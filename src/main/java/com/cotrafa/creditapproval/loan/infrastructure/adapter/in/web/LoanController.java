package com.cotrafa.creditapproval.loan.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.loan.domain.model.LoanReport;
import com.cotrafa.creditapproval.loan.domain.port.in.GetLoanReportUseCase;
import com.cotrafa.creditapproval.loan.infrastructure.adapter.in.web.dto.LoanReportResponse;
import com.cotrafa.creditapproval.loan.infrastructure.adapter.in.web.mapper.LoanReportWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Préstamos",
        description = "Operaciones relacionadas con la gestión y consulta de préstamos aprobados. " +
                "Incluye reportes y estadísticas de los créditos otorgados a clientes."
)
@RestController
@RequestMapping("/api/v1/loan")
@RequiredArgsConstructor
public class LoanController {

    private final GetLoanReportUseCase reportUseCase;
    private final LoanReportWebMapper webMapper;

    @Operation(
            summary = "Obtener reporte de préstamos aprobados",
            description = "Genera un reporte consolidado con el monto total y cantidad de préstamos aprobados. " +
                    "Incluye la suma total de todos los créditos aprobados y el número de préstamos otorgados. " +
                    "Requiere permisos de lectura de préstamos.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Reporte generado exitosamente con estadísticas de préstamos aprobados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoanReportResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para acceder a reportes de préstamos",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/report/total-approved")
    public ResponseEntity<LoanReportResponse> getTotalApproved() {
        LoanReport report = reportUseCase.getApprovedLoansReport();
        return ResponseEntity.ok(webMapper.toResponse(report));
    }
}
