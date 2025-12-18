package com.cotrafa.creditapproval.loanrequeststatus.domain.port.in;

import com.cotrafa.creditapproval.loanrequeststatus.domain.model.LoanRequestStatus;
import java.util.List;
import java.util.UUID;

public interface GetLoanRequestStatusUseCase {
    List<LoanRequestStatus> getAll();
    LoanRequestStatus getByName(String name);
    LoanRequestStatus getById(UUID id);
}