package com.cotrafa.creditapproval.loanrequest.domain.port.out;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequestStatusHistory;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface LoanRequestRepositoryPort {
    LoanRequest save(LoanRequest loanRequest);
    Optional<LoanRequest> findById(UUID id);
    void saveHistory(LoanRequestStatusHistory history);
    Optional<LoanRequestStatusHistory> findCurrentStatusByRequestId(UUID requestId);
    void markPreviousStatusesAsInactive(UUID loanRequestId);
    UUID callAutomaticValidationProcedure(UUID customerId, UUID loanTypeId, BigDecimal amount);
}