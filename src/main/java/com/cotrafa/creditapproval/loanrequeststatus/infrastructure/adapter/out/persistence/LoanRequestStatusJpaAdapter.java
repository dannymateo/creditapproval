package com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanrequeststatus.domain.model.LoanRequestStatus;
import com.cotrafa.creditapproval.loanrequeststatus.domain.port.out.LoanRequestStatusRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoanRequestStatusJpaAdapter implements LoanRequestStatusRepositoryPort {

    private final LoanRequestStatusJpaRepository jpaRepository;
    private final LoanRequestStatusPersistenceMapper mapper;

    @Override
    public List<LoanRequestStatus> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<LoanRequestStatus> findByName(String name) {
        return jpaRepository.findByNameIgnoreCase(name)
                .map(mapper::toDomain);
    }
}