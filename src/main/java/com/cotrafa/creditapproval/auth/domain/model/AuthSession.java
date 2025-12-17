package com.cotrafa.creditapproval.auth.domain.model;

public record AuthSession(
        String accessToken,
        String refreshToken,
        String email,
        String role
) {}
