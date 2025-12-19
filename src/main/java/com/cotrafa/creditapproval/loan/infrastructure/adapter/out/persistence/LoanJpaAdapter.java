package com.cotrafa.creditapproval.loan.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loan.domain.model.Loan;
import com.cotrafa.creditapproval.loan.domain.port.out.LoanRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoanJpaAdapter implements LoanRepositoryPort {

    private final LoanJpaRepository repository;
    private final LoanPersistenceMapper mapper;

    @Override
    public Loan save(Loan loan) {
        LoanJpaEntity entity = mapper.toEntity(loan);
        LoanJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Loan> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Loan> findByLoanRequestId(UUID requestId) {
        return repository.findByLoanRequestId(requestId).map(mapper::toDomain);
    }
}