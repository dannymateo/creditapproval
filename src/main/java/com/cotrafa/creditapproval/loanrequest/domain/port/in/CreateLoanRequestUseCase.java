package com.cotrafa.creditapproval.loanrequest.domain.port.in;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;

import java.util.UUID;

public interface CreateLoanRequestUseCase {
    LoanRequest create(LoanRequest loanRequest, UUID userId);
}