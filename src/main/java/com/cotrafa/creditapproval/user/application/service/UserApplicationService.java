package com.cotrafa.creditapproval.user.application.service;

import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.DatabaseConflictException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import com.cotrafa.creditapproval.user.domain.model.User;
import com.cotrafa.creditapproval.user.domain.port.in.*;
import com.cotrafa.creditapproval.user.domain.port.out.NotificationPort;
import com.cotrafa.creditapproval.user.domain.port.out.PasswordEncoderPort;
import com.cotrafa.creditapproval.user.domain.port.out.UserRepositoryPort;
import com.cotrafa.creditapproval.role.domain.port.out.RoleRepositoryPort;

import com.cotrafa.creditapproval.shared.infrastructure.util.PasswordGeneratorUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserApplicationService implements CreateUserUseCase, UpdateUserUseCase, GetUserUseCase, DeleteUserUseCase, PasswordManagementUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final NotificationPort notificationPort;
    private final PasswordGeneratorUtil passwordGeneratorUtil;

    @Override
    @Transactional
    public User create(User user) {
        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new DatabaseConflictException("Email already exists: " + user.getEmail());
        }

        roleRepositoryPort.findById(user.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + user.getRoleId()));

        String rawPassword = passwordGeneratorUtil.generateStrongPassword();

        user.setLastPasswordChange(Instant.now());

        user.setPassword(passwordEncoderPort.encode(rawPassword));

        User savedUser = userRepositoryPort.save(user);
        notificationPort.sendWelcomeEmail(savedUser, rawPassword);

        return savedUser;
    }

    @Override
    @Transactional
    public User update(UUID id, User user) {
        User existingUser = userRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getEmail() != null &&
                !existingUser.getEmail().equalsIgnoreCase(user.getEmail())) {

            if (userRepositoryPort.existsByEmail(user.getEmail())) {
                throw new DatabaseConflictException("Email already exists");
            }
            existingUser.setEmail(user.getEmail());
        }

        if (user.getActive() != null) existingUser.setActive(user.getActive());

        if (user.getRoleId() != null) {
            roleRepositoryPort.findById(user.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + user.getRoleId()));

            existingUser.setRoleId(user.getRoleId());
        }

        return userRepositoryPort.save(existingUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(UUID id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResult<User> getAll(PaginationCriteria criteria) {
        return userRepositoryPort.findAll(criteria);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        userRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional
    public void resetPasswordByAdmin(UUID userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String newRawPassword = passwordGeneratorUtil.generateStrongPassword();

        user.setPassword(passwordEncoderPort.encode(newRawPassword));
        user.resetLoginAttempts();
        user.setSessionId(null);
        user.setLogged(false);

        userRepositoryPort.save(user);
        notificationPort.sendPasswordResetEmail(user, newRawPassword);
    }
}