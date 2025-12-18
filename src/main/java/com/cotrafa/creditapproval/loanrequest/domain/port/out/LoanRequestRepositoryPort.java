package com.cotrafa.creditapproval.loanrequest.domain.port.out;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequestStatusHistory;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface LoanRequestRepositoryPort {
    LoanRequest save(LoanRequest loanRequest);
    void saveHistory(LoanRequestStatusHistory history);
    void markPreviousStatusesAsInactive(UUID loanRequestId);
    Optional<LoanRequest> findById(UUID id);
    String callAutomaticValidationProcedure(UUID customerId, UUID loanTypeId, BigDecimal amount);
}