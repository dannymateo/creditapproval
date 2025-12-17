package com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RefreshTokenDTO {
    @NotBlank(message = "Refresh token is required")
    @Size(max = 2048, message = "Token is suspiciously long")
    private String refreshToken;
}