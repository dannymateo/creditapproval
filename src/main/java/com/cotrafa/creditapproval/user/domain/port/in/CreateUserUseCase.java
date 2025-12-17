package com.cotrafa.creditapproval.user.domain.port.in;

import com.cotrafa.creditapproval.user.domain.model.User;

public interface CreateUserUseCase {
    User create(User user);
}