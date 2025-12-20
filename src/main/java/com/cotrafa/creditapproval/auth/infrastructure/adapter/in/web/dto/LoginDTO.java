package com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        description = "Datos requeridos para el inicio de sesión de un usuario en el sistema",
        example = "{\"email\": \"usuario@cotrafa.com\", \"password\": \"Password123@\"}"
)
public class LoginDTO {

    @Schema(
            description = "Dirección de correo electrónico del usuario registrado en el sistema. " +
                    "Debe ser un email válido y no exceder los 100 caracteres.",
            example = "usuario@cotrafa.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email is too long")
    private String email;

    @Schema(
            description = "Contraseña del usuario. Debe tener entre 8 y 20 caracteres. " +
                    "Se recomienda incluir mayúsculas, minúsculas, números y caracteres especiales.",
            example = "Password123@",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 8,
            maxLength = 20,
            format = "password"
    )
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;
}