package com.cotrafa.creditapproval.key.domain.port.in;

import java.util.UUID;

public interface ValidateKeyUseCase {
    void validatePasswordResetOtp(UUID userId, String otp);
    void validateApiKey(UUID userId, String apiKey);
}