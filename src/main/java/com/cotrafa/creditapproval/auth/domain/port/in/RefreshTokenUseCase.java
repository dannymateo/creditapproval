package com.cotrafa.creditapproval.auth.domain.port.in;

import com.cotrafa.creditapproval.auth.domain.model.AuthSession;

public interface RefreshTokenUseCase {
    AuthSession refreshToken(String refreshToken);
}