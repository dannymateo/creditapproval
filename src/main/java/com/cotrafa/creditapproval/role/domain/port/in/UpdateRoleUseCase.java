package com.cotrafa.creditapproval.role.domain.port.in;

import com.cotrafa.creditapproval.role.domain.model.Role;

import java.util.UUID;

public interface UpdateRoleUseCase {
    Role update(UUID id, Role role);
}