package com.cotrafa.creditapproval.key.domain.port.in;

import java.util.UUID;

public interface GenerateKeyUseCase {
    String generatePasswordResetOtp(UUID userId);
    String generateApiKey(UUID userId);
}