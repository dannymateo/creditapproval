package com.cotrafa.creditapproval.loan.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loan.domain.model.Loan;
import com.cotrafa.creditapproval.loan.domain.model.LoanReportSummary;
import com.cotrafa.creditapproval.loan.domain.port.out.LoanRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
}