package com.cotrafa.creditapproval.loanrequest.domain.port.out;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequestStatusHistory;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanRequestRepositoryPort {
    LoanRequest save(LoanRequest loanRequest);
    PaginatedResult<LoanRequest> findAll(PaginationCriteria criteria);
    Optional<LoanRequest> findById(UUID id);
    void saveHistory(LoanRequestStatusHistory history);
    Optional<LoanRequestStatusHistory> findCurrentStatusByRequestId(UUID requestId);
    void markPreviousStatusesAsInactive(UUID loanRequestId);
    UUID callAutomaticValidationProcedure(UUID customerId, UUID loanTypeId, BigDecimal amount, Integer termMonths);
    List<LoanRequest> findByCustomerIdAndStatus(UUID customerId, String status);
}