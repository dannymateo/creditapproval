package com.cotrafa.creditapproval.key.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.key.domain.model.Key;
import com.cotrafa.creditapproval.key.domain.port.out.KeyRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KeyJpaAdapter implements KeyRepositoryPort {

    private final KeyJpaRepository jpaRepository;
    private final KeyPersistenceMapper mapper;

    @Override
    public Key save(Key key) {
        KeyJpaEntity entity = mapper.toEntity(key);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Key> findActiveKey(UUID userId, String keyTypeName) {
        return jpaRepository.findActiveKeyByUserAndType(userId, keyTypeName)
                .map(mapper::toDomain);
    }
}