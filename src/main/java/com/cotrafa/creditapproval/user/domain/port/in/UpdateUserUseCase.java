package com.cotrafa.creditapproval.user.domain.port.in;

import com.cotrafa.creditapproval.user.domain.model.User;

import java.util.UUID;

public interface UpdateUserUseCase {
    User update(UUID id, User user);
}