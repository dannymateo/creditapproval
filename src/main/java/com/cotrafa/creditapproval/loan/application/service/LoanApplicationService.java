package com.cotrafa.creditapproval.loan.application.service;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.customer.domain.port.in.GetCustomerUseCase;
import com.cotrafa.creditapproval.loan.domain.model.*;
import com.cotrafa.creditapproval.loan.domain.port.in.CreateLoanUseCase;
import com.cotrafa.creditapproval.loan.domain.port.in.GetLoanReportUseCase;
import com.cotrafa.creditapproval.loan.domain.port.out.LoanRepositoryPort;
import com.cotrafa.creditapproval.loan.domain.port.out.NotificationPort;
import com.cotrafa.creditapproval.loan.domain.service.AmortizationService;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.LoanRequestRepositoryPort;
import com.cotrafa.creditapproval.loanstatus.domain.constants.LoanStatusConstants;
import com.cotrafa.creditapproval.loanstatus.domain.model.LoanStatus;
import com.cotrafa.creditapproval.loanstatus.domain.port.in.GetLoanStatusUseCase;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanApplicationService implements CreateLoanUseCase, GetLoanReportUseCase {

    private final LoanRepositoryPort loanRepositoryPort;
    private final LoanRequestRepositoryPort loanRequestRepositoryPort;
    private final NotificationPort notificationPort;
    private final GetCustomerUseCase customerUseCase;
    private final GetLoanStatusUseCase loanStatusUseCase;

    @Override
    @Transactional
    public Loan createFromRequest(UUID loanRequestId) {
        LoanRequest request = loanRequestRepositoryPort.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found: " + loanRequestId));

        Customer customer = customerUseCase.getById(request.getCustomerId());

        List<LoanInstallment> installments = AmortizationService.calculatePlan(
                request.getAmount(),
                request.getAnnualRate(),
                request.getTermMonths()
        );

        Loan loan = Loan.builder()
                .loanRequestId(request.getId())
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .annualRate(request.getAnnualRate())
                .termMonths(request.getTermMonths())
                .disbursementDate(LocalDate.now())
                .installments(installments)
                .build();

        Loan savedLoan = loanRepositoryPort.save(loan);

        LoanStatus activeStatus = loanStatusUseCase.getByName(LoanStatusConstants.ACTIVE);
        processStatusTransition(savedLoan, activeStatus.getId(), "Initial loan creation from approved request: " + loanRequestId);

        sendApprovalNotification(request, customer, installments);

        return savedLoan;
    }

    @Override
    @Transactional(readOnly = true)
    public LoanReport getApprovedLoansReport() {
        return loanRepositoryPort.getApprovedLoansSummary();
    }

    private void processStatusTransition(Loan loan, UUID statusId, String observation) {
        loanStatusUseCase.getById(statusId);

        loanRepositoryPort.markPreviousStatusesAsInactive(loan.getId());

        LoanStatusHistory history = LoanStatusHistory.builder()
                .loanId(loan.getId())
                .loanStatusId(statusId)
                .assignedAt(LocalDateTime.now())
                .current(true)
                .observation(observation)
                .build();

        // TODO: Implement case when include other status like "FINALIZED"
        loanRepositoryPort.saveHistory(history);
    }

    private void sendApprovalNotification(LoanRequest request, Customer customer, List<LoanInstallment> installments) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customer.getFirstName() + " " + customer.getLastName());
        variables.put("amount", request.getAmount());
        variables.put("annualRate", request.getAnnualRate());
        variables.put("term", request.getTermMonths());
        variables.put("installments", installments);

        Notification notification = Notification.builder()
                .to(customer.getEmail())
                .subject("¡Tu crédito ha sido desembolsado! - Cotrafa")
                .template("loan-approval-template")
                .variables(variables)
                .build();

        notificationPort.send(notification);
    }
}