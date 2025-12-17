package com.cotrafa.creditapproval.user.domain.port.in;

import java.util.UUID;

public interface PasswordManagementUseCase {
    void resetPasswordByAdmin(UUID userId);
}