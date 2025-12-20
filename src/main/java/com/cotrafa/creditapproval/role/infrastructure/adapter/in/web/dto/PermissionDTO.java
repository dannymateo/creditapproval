package com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(
        description = "Define los permisos de un rol sobre una entidad específica del sistema. " +
                "Especifica qué operaciones CRUD puede realizar el rol sobre dicha entidad.",
        example = "{\"systemEntityId\": \"123e4567-e89b-12d3-a456-426614174000\", " +
                "\"canCreate\": true, \"canRead\": true, \"canUpdate\": true, \"canDelete\": false}"
)
public class PermissionDTO {

    @Schema(
            description = "UUID de la entidad del sistema sobre la cual se define el permiso. " +
                    "Ejemplos: User, Role, Customer, LoanRequest, LoanType, etc.",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid"
    )
    @NotNull(message = "System Entity ID is required")
    private UUID systemEntityId;

    @Schema(
            description = "Permiso para crear nuevos registros de esta entidad",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "canCreate status is required")
    private Boolean canCreate;

    @Schema(
            description = "Permiso para leer/consultar registros de esta entidad",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "canRead status is required")
    private Boolean canRead;

    @Schema(
            description = "Permiso para actualizar registros existentes de esta entidad",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "canUpdate status is required")
    private Boolean canUpdate;

    @Schema(
            description = "Permiso para eliminar registros de esta entidad",
            example = "false",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "canDelete status is required")
    private Boolean canDelete;
}
