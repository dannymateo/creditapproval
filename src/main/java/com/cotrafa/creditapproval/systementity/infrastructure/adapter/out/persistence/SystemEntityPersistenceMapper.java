package com.cotrafa.creditapproval.systementity.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import com.cotrafa.creditapproval.systementity.domain.model.SystemEntity;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(config = CentralMapperConfig.class)
public interface SystemEntityPersistenceMapper {
    SystemEntity toDomain(SystemEntityJpaEntity entity);
    SystemEntityJpaEntity toEntity(SystemEntity domain);

    default SystemEntityJpaEntity map(UUID value) {
        if (value == null) {
            return null;
        }
        return SystemEntityJpaEntity.builder().id(value).build();
    }
}