package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(
        description = "Datos para actualizar un tipo de identificación existente. " +
                "Hereda los mismos campos que CreateIdentificationTypeDTO (name y active).",
        example = "{\"name\": \"Cédula de Ciudadanía\", \"active\": false}"
)
public class UpdateIdentificationTypeDTO extends CreateIdentificationTypeDTO {
}
