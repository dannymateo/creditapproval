package com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(
        description = "Respuesta simplificada de tipo de crédito para selectores en formularios de solicitud. " +
                "Incluye ID, nombre y tasa de interés para que el cliente pueda elegir.",
        example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"name\": \"Crédito Personal\", \"annualRate\": 18.5}"
)
public class LoanTypeSelectResponse {

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
}
