package com.cotrafa.creditapproval.auth.domain.port.in;

import com.cotrafa.creditapproval.auth.domain.model.AuthSession;
import com.cotrafa.creditapproval.auth.domain.model.UserCredentials;

public interface LoginUseCase {
    AuthSession login(UserCredentials userCredentials);
}