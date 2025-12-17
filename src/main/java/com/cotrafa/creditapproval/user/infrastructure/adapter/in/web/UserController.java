package com.cotrafa.creditapproval.user.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.PaginationWebMapper;
import com.cotrafa.creditapproval.user.domain.model.User;
import com.cotrafa.creditapproval.user.domain.port.in.*;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginatedResponseDTO;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginationRequestDTO;
import com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto.CreateUserDTO;
import com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto.UpdateUserDTO;
import com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto.UserResponse;
import com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUseCase;
    private final UpdateUserUseCase updateUseCase;
    private final GetUserUseCase getUseCase;
    private final DeleteUserUseCase deleteUseCase;
    private final PasswordManagementUseCase passwordUseCase;
    private final UserMapper userMapper;
    private final PaginationWebMapper paginationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<UserResponse>>> getAll(
            @ModelAttribute PaginationRequestDTO requestDto) {

        PaginationCriteria criteria = paginationMapper.toDomain(requestDto);

        PaginatedResult<User> domainResult = getUseCase.getAll(criteria);

        List<UserResponse> userDtos = domainResult.getItems().stream()
                .map(userMapper::toResponse)
                .toList();

        PaginatedResponseDTO<UserResponse> response = paginationMapper.toResponse(domainResult, userDtos);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable UUID id) {
        User getUser = getUseCase.getById(id);

        UserResponse response = userMapper.toResponse(getUser);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody CreateUserDTO dto) {

        User userDomain = userMapper.toDomain(dto);

        User createdUser = createUseCase.create(userDomain);

        UserResponse response = userMapper.toResponse(createdUser);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserDTO dto
    ) {
        User userDomain = userMapper.toDomain(dto);

        User updatedUser = updateUseCase.update(id, userDomain);

        UserResponse response = userMapper.toResponse(updatedUser);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        deleteUseCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPasswordByAdmin(@PathVariable UUID id) {
        passwordUseCase.resetPasswordByAdmin(id);
        return ResponseEntity.ok(ApiResponse.success("New password generated and sent to user email"));
    }
}