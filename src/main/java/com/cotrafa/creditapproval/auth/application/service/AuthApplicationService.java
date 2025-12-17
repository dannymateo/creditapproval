package com.cotrafa.creditapproval.auth.application.service;

import com.cotrafa.creditapproval.auth.domain.model.AuthSession;
import com.cotrafa.creditapproval.auth.domain.model.PasswordReset;
import com.cotrafa.creditapproval.auth.domain.model.UserCredentials;
import com.cotrafa.creditapproval.auth.domain.port.in.*;
import com.cotrafa.creditapproval.key.domain.port.in.GenerateKeyUseCase;
import com.cotrafa.creditapproval.key.domain.port.in.ValidateKeyUseCase;
import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.role.domain.port.out.RoleRepositoryPort;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.InvalidCredentialsException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import com.cotrafa.creditapproval.user.domain.model.User;
import com.cotrafa.creditapproval.user.domain.port.out.NotificationPort;
import com.cotrafa.creditapproval.user.domain.port.out.PasswordEncoderPort;
import com.cotrafa.creditapproval.user.domain.port.out.UserRepositoryPort;
import com.cotrafa.creditapproval.shared.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthApplicationService implements LoginUseCase, RefreshTokenUseCase, LogoutUseCase, RestorePasswordUseCase, ChangePasswordUseCase {

    private final UserRepositoryPort userRepository;
    private final RoleRepositoryPort roleRepositoryPort;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtUtil jwtUtil;

    private final GenerateKeyUseCase generateKeyUseCase;
    private final ValidateKeyUseCase validateKeyUseCase;

    private final NotificationPort notificationPort;

    @Value("${security.login.max-attempts:5}")
    private int maxAttempts;

    @Value("${security.login.lock-time-minutes:15}")
    private int lockTimeMinutes;

    @Override
    @Transactional(noRollbackFor = InvalidCredentialsException.class)
    public AuthSession login(UserCredentials credentials) {
        User user = userRepository.findByEmail(credentials.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        checkLockStatus(user);

        if (!passwordEncoder.matches(credentials.password(), user.getPassword())) {
            handleFailedLogin(user);
            throw new InvalidCredentialsException("Invalid credentials");
        }

        user.resetLoginAttempts();
        user.setLogged(true);
        user.setLastLogin(Instant.now());

        UUID sessionId = UUID.randomUUID();
        user.setSessionId(sessionId);
        userRepository.save(user);

        return buildAuthSession(user, sessionId, null);
    }

    @Override
    @Transactional
    public AuthSession refreshToken(String refreshToken) {
        if (!jwtUtil.isValid(refreshToken) || !"REFRESH".equalsIgnoreCase(jwtUtil.getTokenType(refreshToken))) {
            throw new InvalidCredentialsException("Invalid token");
        }

        UUID userId = jwtUtil.getUserId(refreshToken);
        UUID sessionId = jwtUtil.getSessionId(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        if (user.getSessionId() == null || !user.getSessionId().equals(sessionId)) {
            throw new InvalidCredentialsException("Session revoked");
        }

        return buildAuthSession(user, sessionId, refreshToken);
    }

    @Override
    @Transactional
    public void logout(UUID userId, UUID sessionId) {
        userRepository.findById(userId).ifPresent(user -> {
            if (user.getSessionId() != null && user.getSessionId().equals(sessionId)) {
                user.setSessionId(null);
                user.setLogged(false);
                userRepository.save(user);
            }
        });
    }

    @Override
    @Transactional
    public void restorePassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String otp = generateKeyUseCase.generatePasswordResetOtp(user.getId());

        notificationPort.sendPasswordResetEmail(user, otp);
    }

    @Override
    @Transactional
    public void changePassword(PasswordReset passwordReset) {
        User user = userRepository.findByEmail(passwordReset.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateKeyUseCase.validatePasswordResetOtp(user.getId(), passwordReset.otp());

        user.setPassword(passwordEncoder.encode(passwordReset.newPassword()));
        user.setLastPasswordChange(Instant.now());
        user.setSessionId(null);
        user.setLogged(false);

        userRepository.save(user);
    }

    private void checkLockStatus(User user) {
        if (user.isLocked()) {
            throw new InvalidCredentialsException("Account locked. Try again later.");
        }
        if (user.getUnlockDate() != null && user.getUnlockDate().isBefore(Instant.now())) {
            user.setUnlockDate(null);
            user.setLoginAttempts(0);
        }
    }

    private void handleFailedLogin(User user) {
        user.setLoginAttempts(user.getLoginAttempts() + 1);
        if (user.getLoginAttempts() >= maxAttempts) {
            user.setUnlockDate(Instant.now().plus(Duration.ofMinutes(lockTimeMinutes)));
        }
        userRepository.save(user);
    }

    private AuthSession buildAuthSession(User user, UUID sessionId, String existingRefreshToken) {
        String accessToken = jwtUtil.createAccessToken(user.getId(), sessionId);
        String refreshToken = (existingRefreshToken != null) ? existingRefreshToken : jwtUtil.createRefreshToken(user.getId(), sessionId);

        return new AuthSession(
                accessToken,
                refreshToken,
                user.getEmail(),
                fetchRoleName(user.getRoleId())
        );
    }

    private String fetchRoleName(UUID roleId) {
        if (roleId == null) return null;
        return roleRepositoryPort.findById(roleId)
                .map(Role::getName)
                .orElse(null);
    }
}