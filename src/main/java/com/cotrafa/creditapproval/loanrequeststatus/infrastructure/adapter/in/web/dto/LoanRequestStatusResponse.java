package com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(
        description = "Representa un estado en el ciclo de vida de una solicitud de crédito. " +
                "Los estados definen las diferentes etapas por las que pasa una solicitud desde su creación hasta su resolución.",
        example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"name\": \"PENDING\"}"
)
public class LoanRequestStatusResponse {

    @Schema(
            description = "Identificador único del estado",
            example = "123e4567-e89b-12d3-a456-426614174000",
            format = "uuid"
    )
    private UUID id;

    @Schema(
            description = "Nombre del estado de la solicitud. " +
                    "Valores comunes: PENDING (Pendiente), IN_REVIEW (En Revisión), " +
                    "APPROVED (Aprobada), REJECTED (Rechazada), CANCELLED (Cancelada). " +
                    "Se utiliza para el control del flujo de aprobación de créditos.",
            example = "PENDING"
    )
    private String name;
}
