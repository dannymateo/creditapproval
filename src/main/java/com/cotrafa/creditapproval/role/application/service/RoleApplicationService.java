package com.cotrafa.creditapproval.role.application.service;

import com.cotrafa.creditapproval.role.domain.model.Permission;
import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.role.domain.port.in.CreateRoleUseCase;
import com.cotrafa.creditapproval.role.domain.port.in.DeleteRoleUseCase;
import com.cotrafa.creditapproval.role.domain.port.in.GetRoleUseCase;
import com.cotrafa.creditapproval.role.domain.port.in.UpdateRoleUseCase;
import com.cotrafa.creditapproval.role.domain.port.out.RoleRepositoryPort;
import com.cotrafa.creditapproval.shared.domain.constants.RoleConstants;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.BadRequestException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.DatabaseConflictException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import com.cotrafa.creditapproval.systementity.domain.port.out.SystemEntityRepositoryPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleApplicationService implements CreateRoleUseCase, UpdateRoleUseCase, GetRoleUseCase, DeleteRoleUseCase {

    private final RoleRepositoryPort roleRepository;
    private final SystemEntityRepositoryPort systemEntityRepository;

    @Override
    @Transactional
    public Role create(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new DatabaseConflictException("Role already exists with name: " + role.getName());
        }

        validatePermissions(role.getPermissions());

        Role roleToSave = role.toBuilder()
                .active(true)
                .build();

        return roleRepository.save(roleToSave);
    }

    @Override
    @Transactional
    public Role update(UUID id, Role roleRequest) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (existingRole.getName().equalsIgnoreCase(RoleConstants.CUSTOMER)) {
            if (roleRequest.getName() != null && !existingRole.getName().equalsIgnoreCase(roleRequest.getName())) {
                throw new BadRequestException("The role name 'CUSTOMER' is required by the system and cannot be changed");
            }
        }

        Role.RoleBuilder builder = existingRole.toBuilder();

        if (roleRequest.getName() != null && !existingRole.getName().equalsIgnoreCase(roleRequest.getName())) {
            if (roleRepository.existsByName(roleRequest.getName())) {
                throw new DatabaseConflictException("Role already exists");
            }
            builder.name(roleRequest.getName());
        }

        if (roleRequest.getActive() != null) {
            builder.active(roleRequest.getActive());
        }

        if (roleRequest.getPermissions() != null) {
            validatePermissions(roleRequest.getPermissions());
            builder.permissions(roleRequest.getPermissions());
        }

        return roleRepository.save(builder.build());
    }

    @Override
    @Transactional(readOnly = true)
    public Role getById(UUID id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResult<Role> getAll(PaginationCriteria criteria) {
        return roleRepository.findAll(criteria);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (role.getName().equalsIgnoreCase(RoleConstants.CUSTOMER)) {
            throw new BadRequestException("The role name 'CUSTOMER' is required by the system and cannot be deleted");
        }

        if (roleRepository.isRoleAssignedToUsers(id)) {
            throw new DatabaseConflictException("Cannot delete role. It is assigned to active users.");
        }

        roleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllActive() {
        return roleRepository.findAllActive();
    }

    private void validatePermissions(List<Permission> permissions) {
        if (permissions == null) return;

        for (Permission permission : permissions) {
            systemEntityRepository.findById(permission.getEntityId())
                    .orElseThrow(() -> new ResourceNotFoundException("SystemEntity not found: " + permission.getEntityId()));
        }
    }
}