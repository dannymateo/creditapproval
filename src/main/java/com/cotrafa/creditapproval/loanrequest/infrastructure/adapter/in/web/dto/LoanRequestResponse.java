package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@Schema(
        description = "Representa una solicitud de crédito con toda la información del solicitante, " +
                "el crédito solicitado y su estado actual en el proceso de aprobación.",
        example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"amount\": 5000000.00, " +
                "\"termMonths\": 36, \"applicantName\": \"Juan Pérez\", " +
                "\"applicantEmail\": \"juan.perez@example.com\", \"applicantSalary\": 3000000.00, " +
                "\"loanTypeName\": \"Crédito de Libre Inversión\", \"status\": \"PENDING\"}"
)
public class LoanRequestResponse {

    @Schema(
            description = "Identificador único de la solicitud de crédito",
            example = "123e4567-e89b-12d3-a456-426614174000",
            format = "uuid"
    )
    private UUID id;

    @Schema(
            description = "Monto del crédito solicitado en pesos colombianos (COP)",
            example = "5000000.00"
    )
    private BigDecimal amount;

    @Schema(
            description = "Plazo de pago solicitado en meses",
            example = "36"
    )
    private Integer termMonths;

    @Schema(
            description = "Nombre completo del solicitante del crédito",
            example = "Juan Pérez"
    )
    private String applicantName;

    @Schema(
            description = "Correo electrónico del solicitante",
            example = "juan.perez@example.com"
    )
    private String applicantEmail;

    @Schema(
            description = "Salario mensual del solicitante en pesos colombianos (COP). " +
                    "Se utiliza para evaluar la capacidad de pago y validación automática.",
            example = "3000000.00"
    )
    private BigDecimal applicantSalary;

    @Schema(
            description = "Nombre del tipo de crédito solicitado (ej: Crédito de Libre Inversión, Crédito Vehicular)",
            example = "Crédito de Libre Inversión"
    )
    private String loanTypeName;

    @Schema(
            description = "Estado actual de la solicitud. " +
                    "Valores posibles: PENDING (Pendiente), IN_REVIEW (En Revisión), " +
                    "APPROVED (Aprobada), REJECTED (Rechazada), CANCELLED (Cancelada)",
            example = "PENDING"
    )
    private String status;
}
