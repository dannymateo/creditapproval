package com.cotrafa.creditapproval.loanrequest.application.service;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.customer.domain.port.in.GetCustomerUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequestStatusHistory;
import com.cotrafa.creditapproval.loanrequest.domain.constants.LoanRequestStatusConstants;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.CreateLoanRequestUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.UpdateLoanRequestStatusUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.NotificationPort;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.LoanRequestRepositoryPort;
import com.cotrafa.creditapproval.loanrequeststatus.domain.model.LoanRequestStatus;
import com.cotrafa.creditapproval.loanrequeststatus.domain.port.in.GetLoanRequestStatusUseCase;
import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.loantype.domain.port.in.GetLoanTypeUseCase;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.BadRequestException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanRequestApplicationService implements CreateLoanRequestUseCase, UpdateLoanRequestStatusUseCase {

    private final LoanRequestRepositoryPort repositoryPort;
    private final GetLoanTypeUseCase loanTypeUseCase;
    private final GetLoanRequestStatusUseCase statusUseCase;
    private final GetCustomerUseCase customerUseCase;
    private final NotificationPort notificationPort;

    @Override
    @Transactional
    public LoanRequest create(LoanRequest loanRequest, UUID userId) {
        Customer customer = customerUseCase.getByUserId(userId);

        LoanType loanType = loanTypeUseCase.getById(loanRequest.getLoanTypeId());

        LoanRequest savedRequest = loanRequest.toBuilder()
                .customerId(customer.getId())
                .annualRate(loanType.getAnnualRate())
                .build();

        notificationPort.sendReceivedEmail(savedRequest);

        UUID statusIdToApply;

        if (loanType.isAutomaticValidation()) {
            statusIdToApply = repositoryPort.callAutomaticValidationProcedure(
                    savedRequest.getCustomerId(),
                    savedRequest.getLoanTypeId(),
                    savedRequest.getAmount());
        } else {
            statusIdToApply = statusUseCase.getByName(LoanRequestStatusConstants.PENDING_REVIEW).getId();
        }

        processStatusTransition(savedRequest, statusIdToApply, "Initial system processing");

        return savedRequest;
    }

    @Override
    @Transactional
    public void updateStatus(UUID requestId, UUID statusId, String observation) {
        LoanRequest request = repositoryPort.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Request not found"));

        LoanRequestStatusHistory currentHistory = repositoryPort.findCurrentStatusByRequestId(requestId)
                .orElseThrow(() -> new IllegalStateException("Request has no current status"));

        LoanRequestStatus currentStatus = statusUseCase.getById(currentHistory.getLoanRequestStatusId());

        // State machine: Only allows updates if it is in PENDING_REVIEW
        if (!currentStatus.getName().equals(LoanRequestStatusConstants.PENDING_REVIEW)) {
            throw new BadRequestException("Only requests in PENDING_REVIEW can be updated.");
        }

        String obs = (observation == null || observation.isBlank()) ? "Manual update by Analyst" : observation;
        processStatusTransition(request, statusId, obs);
    }

    private void processStatusTransition(LoanRequest request, UUID statusId, String observation) {
        LoanRequestStatus status = statusUseCase.getById(statusId);

        repositoryPort.markPreviousStatusesAsInactive(request.getId());
        repositoryPort.saveHistory(LoanRequestStatusHistory.builder()
                .loanRequestId(request.getId())
                .loanRequestStatusId(status.getId())
                .assignedAt(LocalDateTime.now())
                .current(true)
                .observation(observation)
                .build());

        switch (status.getName()) {
            case LoanRequestStatusConstants.APPROVED -> {
                notificationPort.sendApprovedEmail(request);
                // Aquí se llamará al módulo de 'Loan' para generar el crédito y plan de pagos
            }
            case LoanRequestStatusConstants.REJECTED -> notificationPort.sendRejectedEmail(request);
        }
    }
}