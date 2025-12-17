package com.cotrafa.creditapproval.role.domain.port.in;

import java.util.UUID;

public interface DeleteRoleUseCase {
    void delete(UUID id);
}