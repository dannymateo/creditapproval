package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(
        description = "DTO para la creación de una nueva solicitud de crédito. " +
                "El cliente asociado se obtiene automáticamente del usuario autenticado mediante el token JWT. " +
                "El sistema evalúa automáticamente si el tipo de crédito permite validación automática " +
                "basada en el salario del cliente y las reglas configuradas.",
        example = "{\"loanTypeId\": \"123e4567-e89b-12d3-a456-426614174001\", \"amount\": 5000000.00, \"termMonths\": 36}"
)
public class CreateLoanRequestDTO {

    @Schema(
            description = "ID del tipo de crédito solicitado. Debe existir en el catálogo de tipos de crédito " +
                    "y estar activo. El tipo determina la tasa de interés y si se aplica validación automática.",
            example = "123e4567-e89b-12d3-a456-426614174001",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid"
    )
    @NotNull(message = "Loan Type ID is required")
    private UUID loanTypeId;

    @Schema(
            description = "Monto del crédito solicitado en pesos colombianos (COP). " +
                    "Debe ser un valor positivo con máximo 2 decimales. " +
                    "El sistema valida que el monto sea coherente con el salario del cliente " +
                    "según las reglas del tipo de crédito.",
            example = "5000000.00",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0.01",
            maximum = "999999999999999.99"
    )
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be a positive value")
    @Digits(integer = 15, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;

    @Schema(
            description = "Plazo de pago del crédito expresado en meses. " +
                    "Debe ser un número entero positivo. Valores típicos: 12, 24, 36, 48, 60 meses. " +
                    "El plazo afecta el cálculo de las cuotas mensuales del crédito.",
            example = "36",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1"
    )
    @NotNull(message = "Term in months is required")
    @Min(value = 1, message = "Minimum term is 1 month")
    private Integer termMonths;
}
