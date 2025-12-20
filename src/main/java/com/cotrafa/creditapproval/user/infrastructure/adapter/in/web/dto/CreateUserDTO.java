package com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(
        description = "Datos requeridos para crear un nuevo usuario en el sistema. " +
                "El sistema genera automáticamente una contraseña temporal que se envía al email del usuario.",
        example = "{\"email\": \"analista@cotrafa.com\", \"roleId\": \"123e4567-e89b-12d3-a456-426614174000\", \"active\": true}"
)
public class CreateUserDTO {

    @Schema(
            description = "Correo electrónico del usuario. Debe ser único en el sistema y será usado para el inicio de sesión. " +
                    "Se enviará la contraseña temporal a este email.",
            example = "analista@cotrafa.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email is too long")
    private String email;

    @Schema(
            description = "UUID del rol que se asignará al usuario. Define los permisos y accesos que tendrá en el sistema.",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid"
    )
    @NotNull(message = "Role ID is required")
    private UUID roleId;

    @Schema(
            description = "Estado del usuario. Define si puede iniciar sesión en el sistema. " +
                    "true = Activo (puede acceder), false = Inactivo (bloqueado).",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Active status is required")
    private Boolean active;
}
