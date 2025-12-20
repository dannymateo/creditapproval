package com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Schema(
        description = "Respuesta completa con los datos de un rol. " +
                "Incluye ID, nombre, estado y la matriz completa de permisos sobre entidades del sistema.",
        example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"name\": \"Analista de Crédito\", " +
                "\"active\": true, \"permissions\": []}"
)
public class RoleResponse {

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

    @Schema(
            description = "Estado del rol. true = Activo, false = Inactivo",
            example = "true"
    )
    private boolean active;

    @Schema(
            description = "Lista de permisos del rol sobre las entidades del sistema"
    )
    private List<PermissionDTO> permissions;
}
