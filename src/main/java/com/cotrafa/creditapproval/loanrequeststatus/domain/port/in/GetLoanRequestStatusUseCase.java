package com.cotrafa.creditapproval.loanrequeststatus.domain.port.in;

import com.cotrafa.creditapproval.loanrequeststatus.domain.model.LoanRequestStatus;
import java.util.List;

public interface GetLoanRequestStatusUseCase {
    List<LoanRequestStatus> getAll();
    LoanRequestStatus getByName(String name);
}