package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(
        description = "Respuesta completa con los datos de un tipo de identificación. " +
                "Incluye el ID, nombre y estado activo del tipo de identificación.",
        example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", " +
                "\"name\": \"Cédula de Ciudadanía\", \"active\": true}"
)
public class IdentificationTypeResponse {

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

    @Schema(
            description = "Estado del tipo de identificación. true = Activo, false = Inactivo",
            example = "true"
    )
    private boolean active;
}
