package com.cotrafa.creditapproval.user.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.role.infrastructure.adapter.out.persistence.RoleJpaEntity;
import com.cotrafa.creditapproval.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    @Mapping(source = "role.id", target = "roleId")
    User toDomain(UserJpaEntity entity);

    @Mapping(source = "roleId", target = "role")
    UserJpaEntity toEntity(User user);

    default RoleJpaEntity mapRoleIdToEntity(UUID roleId) {
        if (roleId == null) {
            return null;
        }
        return RoleJpaEntity.builder().id(roleId).build();
    }

    default UserJpaEntity mapUserIdToEntity(UUID userId) {
        if (userId == null) {
            return null;
        }
        return UserJpaEntity.builder().id(userId).build();
    }
}