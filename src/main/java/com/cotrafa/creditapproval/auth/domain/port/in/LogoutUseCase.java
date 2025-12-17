package com.cotrafa.creditapproval.auth.domain.port.in;

import java.util.UUID;

public interface LogoutUseCase {
    void logout(UUID userId, UUID sessionId);
}