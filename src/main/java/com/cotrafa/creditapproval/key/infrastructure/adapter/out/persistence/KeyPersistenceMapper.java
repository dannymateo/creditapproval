package com.cotrafa.creditapproval.key.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.key.domain.model.Key;
import com.cotrafa.creditapproval.keytype.infrastructure.adapter.out.persistence.KeyTypePersistenceMapper;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import com.cotrafa.creditapproval.user.infrastructure.adapter.out.persistence.UserPersistenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class, uses = {UserPersistenceMapper.class, KeyTypePersistenceMapper.class})
public interface KeyPersistenceMapper {

    @Mapping(source = "keyType.id", target = "keyTypeId")
    @Mapping(source = "user.id", target = "userId")
    Key toDomain(KeyJpaEntity entity);

    @Mapping(source = "keyTypeId", target = "keyType")
    @Mapping(source = "userId", target = "user")
    KeyJpaEntity toEntity(Key domain);
}