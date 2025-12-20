package com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(
        description = "Respuesta con los datos básicos de un usuario. " +
                "Incluye ID, email, estado activo y el rol asignado. Por seguridad, no incluye la contraseña.",
        example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", " +
                "\"email\": \"analista@cotrafa.com\", \"active\": true, " +
                "\"roleId\": \"987fcdeb-51a2-43d7-9012-345678901234\"}"
)
public class UserResponse {

    @Schema(
            description = "Identificador único del usuario",
            example = "123e4567-e89b-12d3-a456-426614174000",
            format = "uuid"
    )
    private UUID id;

    @Schema(
            description = "Correo electrónico del usuario",
            example = "analista@cotrafa.com"
    )
    private String email;

    @Schema(
            description = "Estado del usuario. true = Activo (puede acceder), false = Inactivo (bloqueado)",
            example = "true"
    )
    private boolean active;

    @Schema(
            description = "UUID del rol asignado al usuario",
            example = "987fcdeb-51a2-43d7-9012-345678901234",
            format = "uuid"
    )
    private UUID roleId;
}
