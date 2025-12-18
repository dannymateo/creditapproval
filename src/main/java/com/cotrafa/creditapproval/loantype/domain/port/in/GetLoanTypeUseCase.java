package com.cotrafa.creditapproval.loantype.domain.port.in;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;

import java.util.List;
import java.util.UUID;

public interface GetLoanTypeUseCase {
    LoanType getById(UUID id);
    PaginatedResult<LoanType> getAll(PaginationCriteria criteria);
    List<LoanType> getAllActive();
}
