package com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.loantype.domain.port.in.CreateLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.DeleteLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.GetLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.UpdateLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.CreateLoanTypeDTO;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.LoanTypeResponse;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.LoanTypeSelectResponse;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.UpdateLoanTypeDTO;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.mapper.LoanTypeMapper;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.PaginationWebMapper;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginatedResponseDTO;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginationRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Tipos de Crédito",
        description = "Operaciones CRUD para gestionar los tipos de crédito disponibles para los clientes " +
                "(Personal, Vivienda, Vehículo, etc.). Incluye configuración de tasas de interés y validación automática."
)
@RestController
@RequestMapping("/api/v1/loan-type")
@RequiredArgsConstructor
public class LoanTypeController {

    private final CreateLoanTypeUseCase createUseCase;
    private final UpdateLoanTypeUseCase updateUseCase;
    private final GetLoanTypeUseCase getUseCase;
    private final DeleteLoanTypeUseCase deleteUseCase;
    private final LoanTypeMapper mapper;
    private final PaginationWebMapper paginationMapper;

    @Operation(
            summary = "Listar todos los tipos de crédito (paginado)",
            description = "Obtiene una lista paginada de todos los tipos de crédito del sistema con sus tasas de interés. " +
                    "Soporta paginación y ordenamiento. Requiere permisos de lectura.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de tipos de crédito obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedResponseDTO.class))
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
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<LoanTypeResponse>>> getAll(
            @Parameter(description = "Parámetros de paginación (página, tamaño, ordenamiento)")
            @ModelAttribute PaginationRequestDTO requestDto
    ) {

        PaginationCriteria criteria = paginationMapper.toDomain(requestDto);

        PaginatedResult<LoanType> domainResult = getUseCase.getAll(criteria);

        List<LoanTypeResponse> loanTypeDtos = domainResult.getItems().stream()
                .map(mapper::toResponse)
                .toList();

        PaginatedResponseDTO<LoanTypeResponse> response = paginationMapper.toResponse(domainResult, loanTypeDtos);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Obtener tipo de crédito por ID",
            description = "Recupera los detalles de un tipo de crédito específico incluyendo su tasa de interés y configuración. " +
                    "Requiere permisos de lectura.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Tipo de crédito encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanTypeResponse.class))
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
                    responseCode = "404",
                    description = "Tipo de crédito no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LoanTypeResponse>> getById(
            @Parameter(description = "UUID del tipo de crédito a consultar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        LoanType domain = getUseCase.getById(id);
        LoanTypeResponse response = mapper.toResponse(domain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Listar tipos de crédito activos",
            description = "Obtiene todos los tipos de crédito activos con sus tasas. " +
                    "Utilizado para que los clientes autenticados seleccionen el tipo de crédito al solicitar."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de tipos de crédito activos obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanTypeSelectResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<LoanTypeSelectResponse>>> getActive() {
        List<LoanTypeSelectResponse> response = getUseCase.getAllActive().stream()
                .map(mapper::toSelectResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Crear nuevo tipo de crédito",
            description = "Registra un nuevo tipo de crédito en el sistema con su tasa de interés anual. " +
                    "Ejemplos: Crédito Personal, Crédito de Vivienda, Crédito de Vehículo. Requiere permisos de creación.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Tipo de crédito creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanTypeResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos o tipo de crédito ya existe",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para crear tipos de crédito",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<LoanTypeResponse>> create(
            @Parameter(description = "Datos del tipo de crédito a crear", required = true)
            @Valid @RequestBody CreateLoanTypeDTO dto
    ) {
        LoanType domainReq = mapper.toDomain(dto);
        LoanType createdDomain = createUseCase.create(domainReq);
        LoanTypeResponse response = mapper.toResponse(createdDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Actualizar tipo de crédito",
            description = "Modifica los datos de un tipo de crédito existente incluyendo su tasa de interés. " +
                    "Requiere permisos de actualización.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Tipo de crédito actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanTypeResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para actualizar tipos de crédito",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Tipo de crédito no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LoanTypeResponse>> update(
            @Parameter(description = "UUID del tipo de crédito a actualizar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Parameter(description = "Nuevos datos del tipo de crédito", required = true)
            @Valid @RequestBody UpdateLoanTypeDTO dto
    ) {
        LoanType domainReq = mapper.toDomain(dto);
        LoanType updatedDomain = updateUseCase.update(id, domainReq);
        LoanTypeResponse response = mapper.toResponse(updatedDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Eliminar tipo de crédito",
            description = "Elimina permanentemente un tipo de crédito del sistema. " +
                    "Solo se puede eliminar si no está siendo utilizado en solicitudes de crédito. " +
                    "Requiere permisos de eliminación.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Tipo de crédito eliminado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"success\": true, \"message\": \"Loan type deleted successfully\", \"data\": \"Loan type deleted successfully\"}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "No se puede eliminar porque está siendo utilizado en solicitudes",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para eliminar tipos de crédito",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Tipo de crédito no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @Parameter(description = "UUID del tipo de crédito a eliminar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        deleteUseCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Loan type deleted successfully"));
    }
}
