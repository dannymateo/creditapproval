package com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.mapper;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.role.domain.model.Permission;
import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.CreateRoleDTO;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.PermissionDTO;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.RoleResponse;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.RoleSelectResponse;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = CentralMapperConfig.class)
public interface RoleMapper {

    Role toDomain(CreateRoleDTO dto);

    @Mapping(target = "entityId", source = "systemEntityId")
    Permission toDomain(PermissionDTO dto);

    RoleResponse toResponse(Role role);

    @Mapping(target = "systemEntityId", source = "entityId")
    PermissionDTO toPermissionDTO(Permission permission);

    RoleSelectResponse toSelectResponse(Role domain);
}