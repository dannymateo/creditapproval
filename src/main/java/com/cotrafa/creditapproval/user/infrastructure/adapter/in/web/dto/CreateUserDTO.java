package com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email is too long")
    private String email;

    @NotNull(message = "Role ID is required")
    private UUID roleId;

    @NotNull(message = "Active status is required")
    private Boolean active;
}