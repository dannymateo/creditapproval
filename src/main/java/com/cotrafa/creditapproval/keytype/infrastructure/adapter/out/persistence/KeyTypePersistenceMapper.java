package com.cotrafa.creditapproval.keytype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.keytype.domain.model.KeyType;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(config = CentralMapperConfig.class)
public interface KeyTypePersistenceMapper {
    KeyTypeJpaEntity toEntity(KeyType domain);

    KeyType toDomain(KeyTypeJpaEntity entity);

    default KeyTypeJpaEntity map(UUID keyTypeId) {
        if (keyTypeId == null) {
            return null;
        }
        return KeyTypeJpaEntity.builder().id(keyTypeId).build();
    }
}