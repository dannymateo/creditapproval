package com.cotrafa.creditapproval.systementity.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.systementity.domain.model.SystemEntity;
import com.cotrafa.creditapproval.systementity.domain.port.out.SystemEntityRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SystemEntityJpaAdapter implements SystemEntityRepositoryPort {

    private final SystemEntityJpaRepository jpaRepository;
    private final SystemEntityPersistenceMapper mapper;

    @Override
    public List<SystemEntity> findAllOrdered() {
        return jpaRepository.findAll(Sort.by(Sort.Direction.ASC, "order", "name"))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<SystemEntity> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}