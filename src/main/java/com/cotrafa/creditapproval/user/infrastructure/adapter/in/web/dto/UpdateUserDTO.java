package com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(
        description = "Datos para actualizar un usuario existente. " +
                "Hereda los mismos campos que CreateUserDTO (email, roleId, active). " +
                "No modifica la contraseña, para eso usar el endpoint de reset password.",
        example = "{\"email\": \"analista@cotrafa.com\", \"roleId\": \"123e4567-e89b-12d3-a456-426614174000\", \"active\": false}"
)
public class UpdateUserDTO extends CreateUserDTO {
}
