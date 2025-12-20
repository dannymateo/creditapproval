package com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(
        description = "Respuesta completa con los datos de un tipo de crédito. " +
                "Incluye ID, nombre, tasa de interés, configuración de validación y estado.",
        example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"name\": \"Crédito Personal\", " +
                "\"annualRate\": 18.5, \"automaticValidation\": false, \"active\": true}"
)
public class LoanTypeResponse {

    @Schema(
            description = "Identificador único del tipo de crédito",
            example = "123e4567-e89b-12d3-a456-426614174000",
            format = "uuid"
    )
    private UUID id;

    @Schema(
            description = "Nombre del tipo de crédito",
            example = "Crédito Personal"
    )
    private String name;

    @Schema(
            description = "Tasa de interés anual en porcentaje",
            example = "18.5"
    )
    private Double annualRate;

    @Schema(
            description = "Indica si se valida automáticamente sin intervención humana",
            example = "false"
    )
    private boolean automaticValidation;

    @Schema(
            description = "Estado del tipo de crédito. true = Activo, false = Inactivo",
            example = "true"
    )
    private boolean active;
}
