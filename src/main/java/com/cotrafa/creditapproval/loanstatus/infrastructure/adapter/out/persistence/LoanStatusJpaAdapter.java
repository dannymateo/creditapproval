package com.cotrafa.creditapproval.loanstatus.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanstatus.domain.model.LoanStatus;
import com.cotrafa.creditapproval.loanstatus.domain.port.out.LoanStatusRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoanStatusJpaAdapter implements LoanStatusRepositoryPort {

    private final LoanStatusJpaRepository repository;
    private final LoanStatusPersistenceMapper mapper;

    @Override
    public Optional<LoanStatus> findByName(String name) {
        return repository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<LoanStatus> findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }
}