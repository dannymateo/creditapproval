package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(
        description = "Respuesta simplificada de tipo de identificación para uso en selectores y formularios. " +
                "Solo incluye el ID y nombre, optimizado para listas desplegables.",
        example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"name\": \"Cédula de Ciudadanía\"}"
)
public class IdentificationTypeSelectResponse {

    @Schema(
            description = "Identificador único del tipo de identificación",
            example = "123e4567-e89b-12d3-a456-426614174000",
            format = "uuid"
    )
    private UUID id;

    @Schema(
            description = "Nombre del tipo de identificación",
            example = "Cédula de Ciudadanía"
    )
    private String name;
}
