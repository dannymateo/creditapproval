package com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(
        description = "Respuesta simplificada de rol para selectores en formularios de usuario. " +
                "Solo incluye ID y nombre, optimizado para listas desplegables.",
        example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"name\": \"Analista de Crédito\"}"
)
public class RoleSelectResponse {

    @Schema(
            description = "Identificador único del rol",
            example = "123e4567-e89b-12d3-a456-426614174000",
            format = "uuid"
    )
    private UUID id;

    @Schema(
            description = "Nombre del rol",
            example = "Analista de Crédito"
    )
    private String name;
}
