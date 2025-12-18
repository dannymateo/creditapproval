package com.cotrafa.creditapproval.user.application.service;

import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.shared.domain.constants.RoleConstants;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.BadRequestException;
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

        validateRole(user.getRoleId());

        String rawPassword = passwordGeneratorUtil.generateStrongPassword();

        User userToSave = user.toBuilder()
                .lastPasswordChange(Instant.now())
                .password(passwordEncoderPort.encode(rawPassword))
                .active(true)
                .loginAttempts(0)
                .logged(false)
                .build();

        User savedUser = userRepositoryPort.save(userToSave);
        notificationPort.sendWelcomeEmail(savedUser, rawPassword);

        return savedUser;
    }

    @Override
    @Transactional
    public User update(UUID id, User userRequest) {
        User existingUser = userRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User.UserBuilder builder = existingUser.toBuilder();

        if (userRequest.getEmail() != null && !existingUser.getEmail().equalsIgnoreCase(userRequest.getEmail())) {
            if (userRepositoryPort.existsByEmail(userRequest.getEmail())) {
                throw new DatabaseConflictException("Email already exists");
            }
            builder.email(userRequest.getEmail());
        }

        if (userRequest.getActive() != null) {
            builder.active(userRequest.getActive());
        }

        if (userRequest.getRoleId() != null && !existingUser.getRoleId().equals(userRequest.getRoleId())) {
            Role currentRole = roleRepositoryPort.findById(existingUser.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Current role not found"));

            if (currentRole.getName().equalsIgnoreCase(RoleConstants.CUSTOMER)) {
                throw new BadRequestException("User with role 'CUSTOMER' cannot be reassigned to a different role.");
            }

            validateRole(userRequest.getRoleId());
            builder.roleId(userRequest.getRoleId());
        }

        return userRepositoryPort.save(builder.build());
    }

    @Override
    @Transactional
    public void resetPasswordByAdmin(UUID userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String newRawPassword = passwordGeneratorUtil.generateStrongPassword();

        User updatedUser = user.toBuilder()
                .password(passwordEncoderPort.encode(newRawPassword))
                .loginAttempts(0)
                .unlockDate(null)
                .sessionId(null)
                .logged(false)
                .lastPasswordChange(Instant.now())
                .build();

        userRepositoryPort.save(updatedUser);
        notificationPort.sendPasswordResetEmail(updatedUser, newRawPassword);
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
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Role role = roleRepositoryPort.findById(user.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (role.getName().equalsIgnoreCase(RoleConstants.CUSTOMER)) {
            throw new BadRequestException("The user cannot be removed because their role is 'CUSTOMER'");
        }

        userRepositoryPort.deleteById(id);
    }

    private void validateRole(UUID roleId) {
        Role role = roleRepositoryPort.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        if (!role.getActive()) {
            throw new BadRequestException("The assigned role '" + role.getName() + "' is currently inactive.");
        }
    }
}