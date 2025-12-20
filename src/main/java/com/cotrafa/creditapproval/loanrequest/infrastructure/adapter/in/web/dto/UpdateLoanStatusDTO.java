package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(
        description = "DTO para actualizar el estado de una solicitud de crédito. " +
                "Permite cambiar el estado (ej: de Pendiente a Aprobada o Rechazada) " +
                "y añadir observaciones sobre la decisión tomada. " +
                "Si se aprueba la solicitud, se crea automáticamente el registro del préstamo.",
        example = "{\"statusId\": \"123e4567-e89b-12d3-a456-426614174002\", " +
                "\"observation\": \"Aprobado: Cliente cumple con todos los requisitos de capacidad de pago\"}"
)
public class UpdateLoanStatusDTO {

    @Schema(
            description = "ID del nuevo estado para la solicitud. " +
                    "Debe ser un estado válido del catálogo de estados de solicitud. " +
                    "Ejemplos: PENDING, IN_REVIEW, APPROVED, REJECTED, CANCELLED",
            example = "123e4567-e89b-12d3-a456-426614174002",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid"
    )
    @NotNull(message = "Status ID is required")
    private UUID statusId;

    @Schema(
            description = "Observación o comentario sobre el cambio de estado. " +
                    "Campo opcional para documentar las razones de aprobación o rechazo. " +
                    "Útil para auditoría y seguimiento. Máximo 500 caracteres.",
            example = "Aprobado: Cliente cumple con todos los requisitos de capacidad de pago",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 500
    )
    @Size(max = 500, message = "Observation is too long (max 500 characters)")
    private String observation;
}
