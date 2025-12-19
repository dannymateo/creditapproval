package com.cotrafa.creditapproval.loanrequest.domain.port.in;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;

public interface GetLoanRequestUseCase {
    PaginatedResult<LoanRequest> getAll(PaginationCriteria criteria);
}
