package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequestStatusHistory;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.LoanRequestRepositoryPort;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoanRequestJpaAdapter implements LoanRequestRepositoryPort {

    private final LoanRequestJpaRepository jpaRepository;
    private final LoanRequestStatusHistoryJpaRepository historyRepository;
    private final LoanRequestPersistenceMapper mapper;
    private final LoanRequestValidationService validationService;

    @Override
    public LoanRequest save(LoanRequest loanRequest) {
        LoanRequestJpaEntity entity = mapper.toEntity(loanRequest);
        LoanRequestJpaEntity savedEntity = jpaRepository.saveAndFlush(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public PaginatedResult<LoanRequest> findAll(PaginationCriteria criteria) {

        Pageable pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                Sort.by("createdAt")
        );

        Page<LoanRequestJpaEntity> entityPage;
        String searchTerm = criteria.getSearch();

        if (StringUtils.hasText(searchTerm)) {
            entityPage = jpaRepository.findByCurrentStatusName(
                    searchTerm, pageable
            );
        } else {
            entityPage = jpaRepository.findAll(pageable);
        }

        List<LoanRequest> domainLoandTypes = entityPage.getContent().stream()
                .map(mapper::toDomain)
                .toList();

        return new PaginatedResult<>(
                domainLoandTypes,
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages()
        );
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
    public UUID callAutomaticValidationProcedure(UUID customerId, UUID loanTypeId, BigDecimal amount, Integer termMonths) {
        return validationService.validateLoanRequest(customerId, loanTypeId, amount, termMonths);
    }
}
