package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface IdentificationTypePersistenceMapper {
    IdentificationType toDomain(IdentificationTypeJpaEntity entity);
    IdentificationTypeJpaEntity toEntity(IdentificationType domain);
}
