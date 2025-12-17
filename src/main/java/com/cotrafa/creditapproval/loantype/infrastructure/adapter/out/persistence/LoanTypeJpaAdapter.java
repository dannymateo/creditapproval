package com.cotrafa.creditapproval.loantype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.loantype.domain.port.out.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoanTypeJpaAdapter implements LoanTypeRepository {

    private final LoanTypeJpaRepository jpaRepository;
    private final LoanTypePersistenceMapper mapper;

    @Override
    public LoanType save(LoanType loanType) {
        LoanTypeJpaEntity entity = mapper.toEntity(loanType);
        LoanTypeJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<LoanType> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<LoanType> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}