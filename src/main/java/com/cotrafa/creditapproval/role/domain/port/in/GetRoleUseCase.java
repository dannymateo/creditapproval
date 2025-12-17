package com.cotrafa.creditapproval.role.domain.port.in;

import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;

import java.util.UUID;

public interface GetRoleUseCase {
    Role getById(UUID id);
    PaginatedResult<Role> getAll(PaginationCriteria criteria);

}