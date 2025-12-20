package com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        description = "Datos requeridos para renovar un token de acceso expirado. " +
                "Permite obtener un nuevo access token sin necesidad de volver a autenticarse con credenciales.",
        example = "{\"refreshToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"
)
public class RefreshTokenDTO {

    @Schema(
            description = "Token de renovación (Refresh Token) obtenido durante el inicio de sesión. " +
                    "Debe ser válido y no haber expirado (válido por 7 días desde su emisión). " +
                    "La sesión asociada debe estar activa en el sistema.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidHlwZSI6IlJFRlJFU0giLCJzaWQiOiJhYmNkLTEyMzQtZWY1Ni03ODkwIiwiaWF0IjoxNTE2MjM5MDIyfQ.signature",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 2048
    )
    @NotBlank(message = "Refresh token is required")
    @Size(max = 2048, message = "Token is suspiciously long")
    private String refreshToken;
}