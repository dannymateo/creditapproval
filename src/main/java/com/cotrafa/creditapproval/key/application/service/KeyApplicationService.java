package com.cotrafa.creditapproval.key.application.service;

import com.cotrafa.creditapproval.key.domain.model.Key;
import com.cotrafa.creditapproval.key.domain.port.in.GenerateKeyUseCase;
import com.cotrafa.creditapproval.key.domain.port.in.ValidateKeyUseCase;
import com.cotrafa.creditapproval.key.domain.port.out.KeyRepositoryPort;
import com.cotrafa.creditapproval.keytype.domain.model.KeyType;
import com.cotrafa.creditapproval.keytype.domain.port.in.FindOrCreateKeyTypeUseCase;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.InvalidCredentialsException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import com.cotrafa.creditapproval.user.domain.model.User;
import com.cotrafa.creditapproval.user.domain.port.out.PasswordEncoderPort;
import com.cotrafa.creditapproval.user.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeyApplicationService implements GenerateKeyUseCase, ValidateKeyUseCase {

    private final KeyRepositoryPort keyRepositoryPort;
    private final FindOrCreateKeyTypeUseCase keyTypeUseCase;
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    private static final SecureRandom secureRandom = new SecureRandom();
    public static final String TYPE_PASSWORD_RESET = "PASSWORD_RESET";
    public static final String TYPE_API_KEY = "API_KEY";

    @Override
    @Transactional
    public String generatePasswordResetOtp(UUID userId) {
        return generateKeyInternal(userId, TYPE_PASSWORD_RESET, true, 6, 15, ChronoUnit.MINUTES);
    }

    @Override
    @Transactional
    public String generateApiKey(UUID userId) {
        return generateKeyInternal(userId, TYPE_API_KEY, false, 32, 365, ChronoUnit.DAYS);
    }

    private String generateKeyInternal(UUID userId, String typeName, boolean isNumeric, int length, int duration, ChronoUnit unit) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        KeyType keyType = keyTypeUseCase.findOrCreate(typeName);

        keyRepositoryPort.findActiveKey(userId, typeName).ifPresent(k -> {
            Key deactivatedKey = k.toBuilder().active(false).build();
            keyRepositoryPort.save(deactivatedKey);
        });

        String rawKey = generateRawKeyValue(isNumeric, length);

        Key key = Key.builder()
                .userId(user.getId())
                .keyTypeId(keyType.getId())
                .key(passwordEncoder.encode(rawKey))
                .active(true)
                .attempts(0)
                .expiredAt(Instant.now().plus(duration, unit))
                .build();

        keyRepositoryPort.save(key);
        return rawKey;
    }

    private String generateRawKeyValue(boolean isNumeric, int length) {
        if (isNumeric) {
            int min = (int) Math.pow(10, length - 1);
            int max = (int) Math.pow(10, length) - 1;
            return String.valueOf(min + secureRandom.nextInt(max - min));
        }
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = InvalidCredentialsException.class)
    public void validatePasswordResetOtp(UUID userId, String otp) {
        validateKeyInternal(userId, otp, TYPE_PASSWORD_RESET, 3, true);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = InvalidCredentialsException.class)
    public void validateApiKey(UUID userId, String apiKey) {
        validateKeyInternal(userId, apiKey, TYPE_API_KEY, 1000, false);
    }

    private void validateKeyInternal(UUID userId, String rawKeyInput, String typeName, int maxAttempts, boolean burnAfterUse) {
        Key key = keyRepositoryPort.findActiveKey(userId, typeName)
                .orElseThrow(() -> new InvalidCredentialsException("No valid key found or key expired."));

        if (key.isExpired()) {
            keyRepositoryPort.save(key.toBuilder().active(false).build());
            throw new InvalidCredentialsException("Key has expired.");
        }

        if (!passwordEncoder.matches(rawKeyInput, key.getKey())) {
            int newAttempts = key.getAttempts() + 1;
            boolean shouldDeactivate = newAttempts >= maxAttempts;

            Key updatedKey = key.toBuilder()
                    .attempts(newAttempts)
                    .active(!shouldDeactivate)
                    .build();

            keyRepositoryPort.save(updatedKey);

            if (shouldDeactivate) {
                throw new InvalidCredentialsException("Max attempts reached. Key invalidated.");
            }
            throw new InvalidCredentialsException("Invalid code. Attempts remaining: " + (maxAttempts - newAttempts));
        }

        Key.KeyBuilder finalKeyBuilder = key.toBuilder();
        if (burnAfterUse) {
            finalKeyBuilder.active(false);
        } else {
            finalKeyBuilder.attempts(0);
        }

        keyRepositoryPort.save(finalKeyBuilder.build());
    }
}