package com.cotrafa.creditapproval.role.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.role.domain.model.Permission;
import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import com.cotrafa.creditapproval.systementity.infrastructure.adapter.out.persistence.SystemEntityPersistenceMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = CentralMapperConfig.class, uses = {SystemEntityPersistenceMapper.class})
public interface RolePersistenceMapper {

    @Mapping(target = "permissions", source = "permissions")
    Role toDomain(RoleJpaEntity entity);

    @Mapping(target = "permissions", source = "permissions")
    RoleJpaEntity toEntity(Role domain);

    @Mapping(source = "entity.id", target = "entityId")
    Permission toDomain(PermissionJpaEntity entity);

    @Mapping(source = "entityId", target = "entity.id")
    @Mapping(target = "role", ignore = true)
    PermissionJpaEntity toEntity(Permission domain);

    List<Permission> toDomainList(List<PermissionJpaEntity> entities);

    List<PermissionJpaEntity> toEntityList(List<Permission> domains);

    @AfterMapping
    default void linkPermissions(@MappingTarget RoleJpaEntity roleEntity) {
        if (roleEntity.getPermissions() != null) {
            roleEntity.getPermissions().forEach(permission -> permission.setRole(roleEntity));
        }
    }
}