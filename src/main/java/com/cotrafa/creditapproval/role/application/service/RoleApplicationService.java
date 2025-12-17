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

        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role update(UUID id, Role role) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (existingRole.getName().equalsIgnoreCase(RoleConstants.CUSTOMER)) {
            if (!existingRole.getName().equalsIgnoreCase(role.getName())) {
                throw new BadRequestException("The role name 'CUSTOMER' is required by the system and cannot be changed");
            }
        }

        if (role.getName() != null &&
                !existingRole.getName().equalsIgnoreCase(role.getName())) {

            if (roleRepository.existsByName(role.getName())) {
                throw new DatabaseConflictException("Role already exists");
            }
            existingRole.setName(role.getName());
        }

        if (role.getActive() != null) existingRole.setActive(role.getActive());

        validatePermissions(role.getPermissions());

        if (role.getPermissions() != null) {
            existingRole.setPermissions(role.getPermissions());
        }

        return roleRepository.save(existingRole);
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

        if (roleRepository.isRoleAssignedToUsers(id)) {
            throw new DatabaseConflictException("Cannot delete role. It is assigned to active users.");
        }

        if (role.getName().equalsIgnoreCase(RoleConstants.CUSTOMER)) {
            throw new BadRequestException("The role name 'CUSTOMER' is required by the system and cannot be deleted");
        }

        roleRepository.deleteById(id);
    }

    private void validatePermissions(List<Permission> permissions) {
        if (permissions == null) return;

        for (Permission permission : permissions) {
            systemEntityRepository.findById(permission.getEntityId())
                    .orElseThrow(() -> new ResourceNotFoundException("SystemEntity not found: " + permission.getEntityId()));
        }
    }
}