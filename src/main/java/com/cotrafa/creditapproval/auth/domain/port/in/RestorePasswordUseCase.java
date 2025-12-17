package com.cotrafa.creditapproval.auth.domain.port.in;


public interface RestorePasswordUseCase {
    void restorePassword(String email);
}