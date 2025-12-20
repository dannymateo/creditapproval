package com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(
        description = "Datos requeridos para crear un nuevo rol en el sistema. " +
                "Define el nombre, estado y la matriz de permisos sobre las entidades del sistema.",
        example = "{\"name\": \"Analista de Crédito\", \"active\": true, \"permissions\": []}"
)
public class CreateRoleDTO {

    @Schema(
            description = "Nombre del rol. Debe ser descriptivo y único. " +
                    "Ejemplos: Administrador, Analista de Crédito, Gerente, Operador.",
            example = "Analista de Crédito",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 50
    )
    @NotBlank(message = "Role name is required")
    @Size(min = 3, max = 50, message = "Role name must be between 3 and 50 characters")
    private String name;

    @Schema(
            description = "Estado del rol. Define si está disponible para asignar a usuarios. " +
                    "true = Activo (disponible), false = Inactivo (no disponible).",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Active status is required")
    private Boolean active;

    @Schema(
            description = "Lista de permisos del rol. Define qué acciones puede realizar sobre cada entidad del sistema. " +
                    "Cada permiso especifica la entidad y las operaciones CRUD permitidas.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Permissions list cannot be null")
    @Valid
    private List<PermissionDTO> permissions;
}
