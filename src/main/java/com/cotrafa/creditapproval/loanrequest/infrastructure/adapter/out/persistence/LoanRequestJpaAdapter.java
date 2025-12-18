package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanrequest.domain.constants.LoanRequestStatusConstants;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequestStatusHistory;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.LoanRequestRepositoryPort;
import com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.out.persistence.LoanRequestStatusJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoanRequestJpaAdapter implements LoanRequestRepositoryPort {

    private final LoanRequestJpaRepository jpaRepository;
    private final LoanRequestStatusHistoryJpaRepository historyRepository;
    private final LoanRequestPersistenceMapper mapper;
    private final LoanRequestStatusJpaRepository statusJpaRepository;

    @Override
    public LoanRequest save(LoanRequest loanRequest) {
        LoanRequestJpaEntity entity = mapper.toEntity(loanRequest);
        LoanRequestJpaEntity savedEntity = jpaRepository.saveAndFlush(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<LoanRequest> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void saveHistory(LoanRequestStatusHistory history) {
        historyRepository.save(mapper.toHistoryEntity(history));
    }

    @Override
    public Optional<LoanRequestStatusHistory> findCurrentStatusByRequestId(UUID requestId) {
        return historyRepository.findByLoanRequestIdAndCurrentTrue(requestId)
                .map(mapper::toHistoryDomain);
    }

    @Override
    public void markPreviousStatusesAsInactive(UUID loanRequestId) {
        historyRepository.deactivateAllByLoanRequestId(loanRequestId);
    }

    @Override
    public UUID callAutomaticValidationProcedure(UUID customerId, UUID loanTypeId, BigDecimal amount) {
        // SIMULATION: If the amount is <= 5M, approve; otherwise, manual review.
        String statusToFind = (amount.compareTo(new BigDecimal("5000000")) <= 0)
                ? LoanRequestStatusConstants.APPROVED
                : LoanRequestStatusConstants.PENDING_REVIEW;

        return statusJpaRepository.findByNameIgnoreCase(statusToFind)
                .orElseThrow(() -> new RuntimeException("Status not found in catalog"))
                .getId();
    }
}
