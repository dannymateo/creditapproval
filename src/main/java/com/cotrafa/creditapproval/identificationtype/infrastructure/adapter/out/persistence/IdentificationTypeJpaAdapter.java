package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.identificationtype.domain.port.out.IdentificationTypeRepository;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.out.persistence.LoanTypeJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdentificationTypeJpaAdapter implements IdentificationTypeRepository {
    private final IdentificationTypeJpaRepository jpaRepository;
    private final IdentificationTypePersistenceMapper mapper;

    @Override
    public IdentificationType save(IdentificationType idType) {
        IdentificationTypeJpaEntity entity = mapper.toEntity(idType);
        IdentificationTypeJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<IdentificationType> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<IdentificationType> findAll() {
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
