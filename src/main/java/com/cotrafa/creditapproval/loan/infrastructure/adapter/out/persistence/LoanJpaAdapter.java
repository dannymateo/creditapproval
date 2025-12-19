package com.cotrafa.creditapproval.loan.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loan.domain.model.Loan;
import com.cotrafa.creditapproval.loan.domain.model.LoanStatusHistory;
import com.cotrafa.creditapproval.loan.domain.port.out.LoanRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoanJpaAdapter implements LoanRepositoryPort {

    private final LoanJpaRepository repository;
    private final LoanStatusHistoryJpaRepository historyRepository;
    private final LoanPersistenceMapper mapper;
    private final LoanStatusHistoryPersistenceMapper historyMapper;

    @Override
    public Loan save(Loan loan) {
        LoanJpaEntity entity = mapper.toEntity(loan);
        LoanJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void saveHistory(LoanStatusHistory history) {
        LoanStatusHistoryJpaEntity entity = historyMapper.toEntity(history);
        historyRepository.save(entity);
    }

    @Override
    public void markPreviousStatusesAsInactive(UUID loanId) {
        historyRepository.markAllAsInactiveByLoanId(loanId);
    }
}