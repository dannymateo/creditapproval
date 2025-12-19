package com.cotrafa.creditapproval.loan.domain.port.in;

import com.cotrafa.creditapproval.loan.domain.model.Loan;

import java.util.UUID;

public interface GetLoanUseCase {
    Loan getByRequestId(UUID requestId);
    Loan getById(UUID loanId);
}
