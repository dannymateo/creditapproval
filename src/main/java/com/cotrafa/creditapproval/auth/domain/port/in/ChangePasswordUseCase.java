package com.cotrafa.creditapproval.auth.domain.port.in;

import com.cotrafa.creditapproval.auth.domain.model.PasswordReset;

public interface ChangePasswordUseCase {
    void changePassword(PasswordReset passwordReset);
}