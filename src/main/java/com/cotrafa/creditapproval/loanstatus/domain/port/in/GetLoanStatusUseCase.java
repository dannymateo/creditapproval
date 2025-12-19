package com.cotrafa.creditapproval.loanstatus.domain.port.in;

import com.cotrafa.creditapproval.loanrequeststatus.domain.model.LoanRequestStatus;

import java.util.List;
import java.util.UUID;

public interface GetLoanStatusUseCase {
    LoanRequestStatus getByName(String name);
    LoanRequestStatus getById(UUID id);
}