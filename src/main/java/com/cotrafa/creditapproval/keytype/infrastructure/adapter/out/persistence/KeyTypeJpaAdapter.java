package com.cotrafa.creditapproval.keytype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.keytype.domain.model.KeyType;
import com.cotrafa.creditapproval.keytype.domain.port.out.KeyTypeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KeyTypeJpaAdapter implements KeyTypeRepositoryPort {

    private final KeyTypeJpaRepository jpaRepository;
    private final KeyTypePersistenceMapper mapper;

    @Override
    public Optional<KeyType> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public KeyType save(KeyType keyType) {
        KeyTypeJpaEntity entity = mapper.toEntity(keyType);
        KeyTypeJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}