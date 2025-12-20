package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.CreateLoanRequestUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.GetAiAdviceUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.GetLoanRequestUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.UpdateLoanRequestStatusUseCase;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto.CreateLoanRequestDTO;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto.LoanRequestResponse;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto.UpdateLoanStatusDTO;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.mapper.LoanRequestMapper;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.PaginationWebMapper;
import com.cotrafa.creditapproval.shared.infrastructure.security.JwtUtil;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Solicitudes de Crédito",
        description = "Gestión completa del ciclo de vida de las solicitudes de crédito. " +
                "Permite crear nuevas solicitudes asociadas al cliente autenticado, consultar el historial " +
                "con paginación y filtros, y actualizar el estado de aprobación/rechazo. " +
                "Los estados posibles son: Pendiente, En Revisión, Aprobada, Rechazada."
)
@RestController
@RequestMapping("/api/v1/loan-request")
@RequiredArgsConstructor
public class LoanRequestController {

    private final CreateLoanRequestUseCase createUseCase;
    private final UpdateLoanRequestStatusUseCase updateStatusUseCase;
    private final GetLoanRequestUseCase getUseCase;
    private final GetAiAdviceUseCase getAiAdviceUseCase;
    private final LoanRequestMapper loanRequestMapper;
    private final JwtUtil jwtUtil;
    private final PaginationWebMapper paginationMapper;

    @Operation(
            summary = "Listar todas las solicitudes de crédito con paginación",
            description = "Obtiene el listado paginado de todas las solicitudes de crédito registradas en el sistema. " +
                    "Incluye información del solicitante, monto, plazo, tipo de crédito y estado actual. " +
                    "Soporta ordenamiento, filtrado y paginación para facilitar la gestión de grandes volúmenes. " +
                    "Requiere permisos de lectura sobre solicitudes de crédito.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de solicitudes obtenida exitosamente con metadatos de paginación",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaginatedResponseDTO.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Parámetros de paginación inválidos",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para consultar solicitudes de crédito",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<LoanRequestResponse>>> getAll(
            @Parameter(description = "Parámetros de paginación, ordenamiento y filtrado")
            @ModelAttribute PaginationRequestDTO requestDto) {

        PaginationCriteria criteria = paginationMapper.toDomain(requestDto);
        PaginatedResult<LoanRequest> domainResult = getUseCase.getAll(criteria);

        List<LoanRequestResponse> loanRequestDtos = domainResult.getItems().stream()
                .map(loanRequestMapper::toResponse)
                .toList();

        PaginatedResponseDTO<LoanRequestResponse> response = paginationMapper.toResponse(domainResult, loanRequestDtos);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Obtener asesoramiento de IA para una solicitud",
            description = "Genera un análisis inteligente utilizando modelos de lenguaje (LLM) sobre la viabilidad de la solicitud. " +
                    "El análisis considera el salario del cliente, el monto solicitado, el plazo y las reglas de riesgo de la cooperativa. " +
                    "Proporciona una recomendación narrativa para apoyar la toma de decisiones de los analistas humanos.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Asesoramiento de IA generado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Basado en el salario de 4M y la cuota de 300k, el cliente tiene una capacidad holgada (25% ocupado)...")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "La solicitud de crédito no existe",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "503",
                    description = "El servicio de IA no está disponible temporalmente",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}/ai-advice")
    public ResponseEntity<ApiResponse<String>> getAiAdvice(
            @Parameter(description = "ID único de la solicitud de crédito a analizar", required = true)
            @PathVariable UUID id) {
        String advice = getAiAdviceUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(advice));
    }

    @Operation(
            summary = "Crear una nueva solicitud de crédito",
            description = "Registra una nueva solicitud de crédito asociada al cliente autenticado. " +
                    "El sistema valida automáticamente si el tipo de crédito permite validación automática " +
                    "basada en el salario del cliente y las reglas de negocio configuradas. " +
                    "La solicitud inicia en estado 'Pendiente' o puede ser aprobada/rechazada automáticamente. " +
                    "El usuario se obtiene del token JWT del header Authorization.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Solicitud de crédito creada exitosamente. " +
                            "El estado inicial depende de la validación automática.",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos: monto negativo, plazo inválido, tipo de crédito no encontrado, " +
                            "o validaciones de negocio fallidas",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Token de autorización faltante o inválido. " +
                            "Debe incluirse en el header 'Authorization' con formato 'Bearer {token}'",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para crear solicitudes de crédito",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Tipo de crédito no encontrado o cliente no asociado al usuario",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor durante la creación",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<LoanRequest>> create(
            @Parameter(description = "Datos de la nueva solicitud de crédito: tipo, monto y plazo", required = true)
            @Valid @RequestBody CreateLoanRequestDTO dto,
            HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            UUID userId = jwtUtil.getUserId(token);

            LoanRequest domain = loanRequestMapper.toDomain(dto);

            createUseCase.create(domain, userId);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(null,201));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Authorization token is missing or invalid", 401));
    }

    @Operation(
            summary = "Actualizar el estado de una solicitud de crédito",
            description = "Actualiza el estado de aprobación de una solicitud específica. " +
                    "Permite cambiar entre estados: Pendiente, En Revisión, Aprobada, Rechazada. " +
                    "Se puede incluir una observación opcional (máx 500 caracteres) para documentar " +
                    "las razones de aprobación o rechazo. Si se aprueba, se crea automáticamente " +
                    "un registro de préstamo en el sistema. Requiere permisos de actualización.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Estado actualizado exitosamente. " +
                            "Si fue aprobada, se generó el préstamo asociado.",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos: observación muy larga o transición de estado no permitida",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para actualizar el estado de solicitudes",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Solicitud o estado no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @Parameter(description = "ID único de la solicitud de crédito a actualizar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Parameter(description = "Nuevo estado y observación opcional para la solicitud", required = true)
            @Valid @RequestBody UpdateLoanStatusDTO dto) {
        updateStatusUseCase.updateStatus(id, dto.getStatusId(), dto.getObservation());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
