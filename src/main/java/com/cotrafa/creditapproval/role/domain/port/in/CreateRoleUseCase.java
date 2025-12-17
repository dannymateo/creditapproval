package com.cotrafa.creditapproval.role.domain.port.in;

import com.cotrafa.creditapproval.role.domain.model.Role;

public interface CreateRoleUseCase {
    Role create(Role role);
}