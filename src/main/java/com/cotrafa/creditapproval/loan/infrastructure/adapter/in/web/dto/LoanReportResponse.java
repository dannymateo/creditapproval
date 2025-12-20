package com.cotrafa.creditapproval.loan.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@Schema(
        description = "Reporte consolidado de préstamos aprobados. Contiene estadísticas sobre el monto total " +
                "y la cantidad de créditos otorgados por la financiera.",
        example = "{\"totalAmountApproved\": 125000000.00, \"totalLoansCount\": 45, " +
                "\"currency\": \"COP\", \"generatedAt\": \"2025-12-19T15:30:00\"}"
)
public class LoanReportResponse {

    @Schema(
            description = "Monto total acumulado de todos los préstamos aprobados en pesos colombianos. " +
                    "Representa la suma de todos los créditos otorgados a los clientes.",
            example = "125000000.00"
    )
    private final BigDecimal totalAmountApproved;

    @Schema(
            description = "Cantidad total de préstamos aprobados. Número de créditos otorgados.",
            example = "45"
    )
    private final Long totalLoansCount;

    @Schema(
            description = "Código de moneda en formato ISO 4217. Por defecto COP (Pesos Colombianos).",
            example = "COP"
    )
    private final String currency;

    @Schema(
            description = "Fecha y hora en que se generó el reporte",
            example = "2025-12-19T15:30:00",
            format = "date-time"
    )
    private final LocalDateTime generatedAt;
}