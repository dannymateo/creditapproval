package com.cotrafa.creditapproval.user.domain.port.in;

import java.util.UUID;

public interface DeleteUserUseCase {
    void delete(UUID id);
}