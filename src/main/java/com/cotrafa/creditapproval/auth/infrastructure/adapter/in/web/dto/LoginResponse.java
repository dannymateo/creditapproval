package com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Respuesta del sistema tras un inicio de sesión exitoso o renovación de token. " +
                "Contiene los tokens JWT necesarios para autenticación y la información básica del usuario.",
        example = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", " +
                "\"refreshToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", " +
                "\"email\": \"usuario@cotrafa.com\", " +
                "\"role\": \"Analista\"}"
)
public class LoginResponse {

    @Schema(
            description = "Token de acceso JWT (Access Token) con validez de 30 minutos. " +
                    "Debe incluirse en el header Authorization con el prefijo 'Bearer ' para acceder a endpoints protegidos.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String token;

    @Schema(
            description = "Token de renovación (Refresh Token) con validez de 7 días. " +
                    "Utilízalo en el endpoint /refresh-token para obtener un nuevo access token sin volver a autenticarte.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidHlwZSI6IlJFRlJFU0giLCJpYXQiOjE1MTYyMzkwMjJ9.4Adcj_KlJJXz8E9RCgPz2Y4q0vFXWRwxFvqSxNH9y0g",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String refreshToken;

    @Schema(
            description = "Correo electrónico del usuario autenticado",
            example = "usuario@cotrafa.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
            description = "Rol asignado al usuario en el sistema. Define los permisos y accesos disponibles.",
            example = "Analista de Crédito",
            nullable = true
    )
    private String role;
}