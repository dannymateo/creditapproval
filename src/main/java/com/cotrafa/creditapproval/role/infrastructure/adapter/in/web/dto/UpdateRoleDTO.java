package com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(
        description = "Datos para actualizar un rol existente. " +
                "Hereda los mismos campos que CreateRoleDTO (name, active, permissions).",
        example = "{\"name\": \"Analista de Crédito Senior\", \"active\": true, \"permissions\": []}"
)
public class UpdateRoleDTO extends CreateRoleDTO {
}
