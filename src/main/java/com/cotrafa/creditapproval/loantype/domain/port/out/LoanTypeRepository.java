package com.cotrafa.creditapproval.loantype.domain.port.out;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanTypeRepository {
    LoanType save(LoanType loanType);
    Optional<LoanType> findById(UUID id);
    PaginatedResult<LoanType> findAll(PaginationCriteria criteria);
    List<LoanType> findAllActive();
    boolean existsByName(String name);
    void deleteById(UUID id);
    boolean isLoanTypeAssignedToLoanRequests(UUID loanTypeId);
}