package com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.loanrequeststatus.domain.port.in.GetLoanRequestStatusUseCase;
import com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.in.web.dto.LoanRequestStatusResponse;
import com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.in.web.mapper.LoanRequestStatusMapper;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Estados de Solicitud de Crédito",
        description = "Consulta los estados disponibles para las solicitudes de crédito. " +
                "Los estados representan el ciclo de vida de una solicitud: Pendiente, Aprobada, Rechazada, etc. " +
                "Se utiliza principalmente en filtros y para mostrar opciones en la interfaz."
)
@RestController
@RequestMapping("/api/v1/loan-request-status")
@RequiredArgsConstructor
public class LoanRequestStatusController {

    private final GetLoanRequestStatusUseCase getUseCase;
    private final LoanRequestStatusMapper mapper;

    @Operation(
            summary = "Listar todos los estados de solicitud de crédito",
            description = "Obtiene la lista completa de estados posibles para una solicitud de crédito. " +
                    "Incluye estados como: Pendiente, En Revisión, Aprobada, Rechazada, Cancelada. " +
                    "Esta información se utiliza en los formularios de actualización de solicitudes " +
                    "y en filtros de búsqueda. Requiere permisos de lectura o actualización de solicitudes.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de estados obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoanRequestStatusResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para acceder a este recurso",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<LoanRequestStatusResponse>>> getAll() {
        List<LoanRequestStatusResponse> response = getUseCase.getAll().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
