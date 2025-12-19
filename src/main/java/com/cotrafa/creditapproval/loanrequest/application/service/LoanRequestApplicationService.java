package com.cotrafa.creditapproval.loanrequest.application.service;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.customer.domain.port.in.GetCustomerUseCase;
import com.cotrafa.creditapproval.loan.domain.port.in.CreateLoanUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequestStatusHistory;
import com.cotrafa.creditapproval.loanrequest.domain.constants.LoanRequestStatusConstants;
import com.cotrafa.creditapproval.loanrequest.domain.model.NotificationData;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.CreateLoanRequestUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.GetLoanRequestUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.UpdateLoanRequestStatusUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.NotificationPort;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.LoanRequestRepositoryPort;
import com.cotrafa.creditapproval.loanrequeststatus.domain.model.LoanRequestStatus;
import com.cotrafa.creditapproval.loanrequeststatus.domain.port.in.GetLoanRequestStatusUseCase;
import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.loantype.domain.port.in.GetLoanTypeUseCase;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.BadRequestException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanRequestApplicationService implements CreateLoanRequestUseCase, UpdateLoanRequestStatusUseCase, GetLoanRequestUseCase {

    private final LoanRequestRepositoryPort repositoryPort;
    private final GetLoanTypeUseCase loanTypeUseCase;
    private final GetLoanRequestStatusUseCase statusUseCase;
    private final GetCustomerUseCase customerUseCase;
    private final CreateLoanUseCase createLoanUseCase;
    private final NotificationPort notificationPort;

    @Override
    @Transactional
    public LoanRequest create(LoanRequest loanRequest, UUID userId) {
        Customer customer = customerUseCase.getByUserId(userId);
        LoanType loanType = loanTypeUseCase.getById(loanRequest.getLoanTypeId());

        LoanRequest savedRequest = repositoryPort.save(loanRequest.toBuilder()
                .customerId(customer.getId())
                .annualRate(loanType.getAnnualRate())
                .build());

        UUID statusIdToApply;
        String observation;

        if (loanType.isAutomaticValidation()) {
            statusIdToApply = repositoryPort.callAutomaticValidationProcedure(
                    savedRequest.getCustomerId(),
                    savedRequest.getLoanTypeId(),
                    savedRequest.getAmount());
            observation = "Automatic system validation";
        } else {
            statusIdToApply = statusUseCase.getByName(LoanRequestStatusConstants.PENDING_REVIEW).getId();
            observation = "Initial system processing - Waiting for Analyst";
        }

        processStatusTransition(savedRequest, statusIdToApply, observation, customer, loanType);

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

        if (!currentStatus.getName().equals(LoanRequestStatusConstants.PENDING_REVIEW)) {
            throw new BadRequestException("Only requests in PENDING_REVIEW can be updated.");
        }

        Customer customer = customerUseCase.getById(request.getCustomerId());
        LoanType loanType = loanTypeUseCase.getById(request.getLoanTypeId());

        String obs = (observation == null || observation.isBlank()) ? "Manual update by Analyst" : observation;

        processStatusTransition(request, statusId, obs, customer, loanType);
    }

    private void processStatusTransition(LoanRequest request, UUID statusId, String observation, Customer customer, LoanType loanType) {
        LoanRequestStatus status = statusUseCase.getById(statusId);

        repositoryPort.markPreviousStatusesAsInactive(request.getId());
        repositoryPort.saveHistory(LoanRequestStatusHistory.builder()
                .loanRequestId(request.getId())
                .loanRequestStatusId(status.getId())
                .assignedAt(LocalDateTime.now())
                .current(true)
                .observation(observation)
                .build());

        NotificationData notificationData = buildNotificationData(request, customer, loanType);

        switch (status.getName()) {
            case LoanRequestStatusConstants.APPROVED -> {
                notificationPort.sendApprovedEmail(notificationData);
                createLoanUseCase.createFromRequest(request.getId());
            }
            case LoanRequestStatusConstants.REJECTED -> {
                notificationPort.sendRejectedEmail(notificationData);
            }
            case LoanRequestStatusConstants.PENDING_REVIEW -> {
                notificationPort.sendReceivedEmail(notificationData);
            }
        }
    }

    private NotificationData buildNotificationData(LoanRequest request, Customer customer, LoanType loanType) {
        return NotificationData.builder()
                .toEmail(customer.getEmail())
                .customerName(customer.getFirstName() + " " + customer.getLastName())
                .loanTypeName(loanType.getName())
                .amount(request.getAmount())
                .requestId(request.getId().toString())
                .build();
    }

    @Override
    public PaginatedResult<LoanRequest> getAll(PaginationCriteria criteria) {
        return repositoryPort.findAll(criteria);
    }
}