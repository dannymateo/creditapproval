package com.cotrafa.creditapproval.user.domain.port.in;

import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.user.domain.model.User;
import java.util.UUID;

public interface GetUserUseCase {
    User getById(UUID id);
    PaginatedResult<User> getAll(PaginationCriteria criteria);
}