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

        validateRoleActive(user.getRoleId());

        User checkedUser = checkLockStatus(user);

        if (!passwordEncoder.matches(credentials.password(), checkedUser.getPassword())) {
            handleFailedLogin(checkedUser);
            throw new InvalidCredentialsException("Invalid credentials");
        }

        UUID sessionId = UUID.randomUUID();

        User loggedUser = checkedUser.toBuilder()
                .loginAttempts(0)
                .unlockDate(null)
                .logged(true)
                .lastLogin(Instant.now())
                .sessionId(sessionId)
                .build();

        userRepository.save(loggedUser);

        return buildAuthSession(loggedUser, sessionId, null);
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

        validateRoleActive(user.getRoleId());

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
                User logoutUser = user.toBuilder()
                        .sessionId(null)
                        .logged(false)
                        .build();
                userRepository.save(logoutUser);
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

        User updatedUser = user.toBuilder()
                .password(passwordEncoder.encode(passwordReset.newPassword()))
                .lastPasswordChange(Instant.now())
                .sessionId(null)
                .logged(false)
                .build();

        userRepository.save(updatedUser);
    }

    private User checkLockStatus(User user) {
        if (user.isLocked()) {
            throw new InvalidCredentialsException("Account locked. Try again later.");
        }
        if (user.getUnlockDate() != null && user.getUnlockDate().isBefore(Instant.now())) {
            User resetUser = user.toBuilder()
                    .unlockDate(null)
                    .loginAttempts(0)
                    .build();
            return userRepository.save(resetUser);
        }
        return user;
    }

    private void handleFailedLogin(User user) {
        int newAttempts = user.getLoginAttempts() + 1;
        User.UserBuilder userBuilder = user.toBuilder().loginAttempts(newAttempts);

        if (newAttempts >= maxAttempts) {
            userBuilder.unlockDate(Instant.now().plus(Duration.ofMinutes(lockTimeMinutes)));
        }

        userRepository.save(userBuilder.build());
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

    private void validateRoleActive(UUID roleId) {
        Role role = roleRepositoryPort.findById(roleId)
                .orElseThrow(() -> new InvalidCredentialsException("Assigned role not found"));

        if (!role.getActive()) {
            throw new InvalidCredentialsException("The assigned role '" + role.getName() + "' is currently inactive.");
        }
    }
}