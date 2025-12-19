package com.cotrafa.creditapproval.loanstatus.domain.port.in;

import com.cotrafa.creditapproval.loanstatus.domain.model.LoanStatus;

import java.util.UUID;

public interface GetLoanStatusUseCase {
    LoanStatus getByName(String name);
    LoanStatus getById(UUID id);
}