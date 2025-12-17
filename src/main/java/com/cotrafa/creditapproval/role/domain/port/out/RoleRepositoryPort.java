package com.cotrafa.creditapproval.role.domain.port.out;

import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepositoryPort {
    Role save(Role role);
    Optional<Role> findById(UUID id);
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
    void deleteById(UUID id);
    boolean isRoleAssignedToUsers(UUID roleId);
    PaginatedResult<Role> findAll(PaginationCriteria criteria);
}