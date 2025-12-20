package com.cotrafa.creditapproval.loanrequest.application.service;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.customer.domain.port.in.GetCustomerUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.GetAiAdviceUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.SearchLoanRequestsUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.AiAdvisorPort;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.LoanRequestRepositoryPort;
import com.cotrafa.creditapproval.loanrequeststatus.domain.constants.LoanRequestStatusConstants;
import com.cotrafa.creditapproval.loanrequeststatus.domain.port.in.GetLoanRequestStatusUseCase;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.InvalidCredentialsException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAiAdviceService implements GetAiAdviceUseCase {

    private final LoanRequestRepositoryPort repositoryPort;
    private final GetCustomerUseCase customerUseCase;
    private final GetLoanRequestStatusUseCase statusUseCase;
    private final SearchLoanRequestsUseCase searchLoanRequestsUseCase;
    private final AiAdvisorPort aiAdvisorPort;


    @Override
    @Transactional(readOnly = true)
    public String execute(UUID requestId) {
        LoanRequest request = repositoryPort.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        Customer customer = customerUseCase.getById(request.getCustomerId());

        String statusName = repositoryPort.findCurrentStatusByRequestId(requestId)
                .map(history -> statusUseCase.getById(history.getLoanRequestStatusId()).getName())
                .orElse(LoanRequestStatusConstants.PENDING_REVIEW);

        List<LoanRequest> activeLoans = searchLoanRequestsUseCase.searchByCustomerAndStatus(
                customer.getId(),
                LoanRequestStatusConstants.APPROVED
        );

        // Pasamos la lista al adaptador de IA
        return aiAdvisorPort.getLoanAdvice(request, customer, activeLoans, statusName);
    }
}