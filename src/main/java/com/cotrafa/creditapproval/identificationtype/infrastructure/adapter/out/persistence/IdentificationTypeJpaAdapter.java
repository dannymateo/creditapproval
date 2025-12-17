package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.identificationtype.domain.port.out.IdentificationTypeRepository;
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
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(idType)));
    }

    @Override
    public Optional<IdentificationType> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<IdentificationType> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public void deleteById(UUID id) { jpaRepository.deleteById(id); }
}
