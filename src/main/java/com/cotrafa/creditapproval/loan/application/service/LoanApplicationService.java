package com.cotrafa.creditapproval.loan.application.service;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.customer.domain.port.in.GetCustomerUseCase;
import com.cotrafa.creditapproval.loan.domain.model.Loan;
import com.cotrafa.creditapproval.loan.domain.model.LoanInstallment;
import com.cotrafa.creditapproval.loan.domain.model.Notification;
import com.cotrafa.creditapproval.loan.domain.port.in.CreateLoanUseCase;
import com.cotrafa.creditapproval.loan.domain.port.out.LoanRepositoryPort;
import com.cotrafa.creditapproval.loan.domain.port.out.NotificationPort;
import com.cotrafa.creditapproval.loan.domain.service.AmortizationService;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.LoanRequestRepositoryPort;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanApplicationService implements CreateLoanUseCase {

    private final LoanRepositoryPort loanRepositoryPort;
    private final LoanRequestRepositoryPort loanRequestRepositoryPort;
    private final NotificationPort notificationPort;
    private final GetCustomerUseCase customerUseCase;

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

        sendApprovalNotification(request, customer, installments);

        return savedLoan;
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