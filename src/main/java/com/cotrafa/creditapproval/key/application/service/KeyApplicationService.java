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
            k.setActive(false);
            keyRepositoryPort.save(k);
        });

        String rawKey;
        if (isNumeric) {
            int min = (int) Math.pow(10, length - 1);
            int max = (int) Math.pow(10, length) - 1;
            rawKey = String.valueOf(min + secureRandom.nextInt(max - min));
        } else {
            byte[] randomBytes = new byte[length];
            secureRandom.nextBytes(randomBytes);
            rawKey = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        }

        Key key = new Key();
        key.setUserId(user.getId());
        key.setKeyTypeId(keyType.getId());
        key.setKey(passwordEncoder.encode(rawKey));
        key.setActive(true);
        key.setAttempts(0);
        key.setExpiredAt(Instant.now().plus(duration, unit));

        keyRepositoryPort.save(key);

        return rawKey;
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
            key.setActive(false);
            keyRepositoryPort.save(key);
            throw new InvalidCredentialsException("Key has expired.");
        }

        if (!passwordEncoder.matches(rawKeyInput, key.getKey())) {
            key.incrementAttempts();

            if (key.getAttempts() >= maxAttempts) {
                key.setActive(false);
                keyRepositoryPort.save(key);
                throw new InvalidCredentialsException("Max attempts reached. Key invalidated.");
            }

            keyRepositoryPort.save(key);
            throw new InvalidCredentialsException("Invalid code. Attempts remaining: " + (maxAttempts - key.getAttempts()));
        }

        if (burnAfterUse) {
            key.setActive(false);
        } else {
            key.setAttempts(0);
        }
        keyRepositoryPort.save(key);
    }
}