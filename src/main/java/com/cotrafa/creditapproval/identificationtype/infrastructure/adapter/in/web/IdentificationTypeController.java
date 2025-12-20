package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.CreateIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.DeleteIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.GetIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.UpdateIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.CreateIdentificationTypeDTO;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.IdentificationTypeResponse;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.IdentificationTypeSelectResponse;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.UpdateIdentificationTypeDTO;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.mapper.IdentificationTypeMapper;
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
        name = "Tipos de Identificación",
        description = "Operaciones CRUD para gestionar los tipos de identificación disponibles en el sistema " +
                "(Cédula de Ciudadanía, NIT, Pasaporte, etc.). Utilizado para identificar clientes."
)
@RestController
@RequestMapping("/api/v1/identification-type")
@RequiredArgsConstructor
public class IdentificationTypeController {

    private final CreateIdentificationTypeUseCase createUseCase;
    private final UpdateIdentificationTypeUseCase updateUseCase;
    private final GetIdentificationTypeUseCase getUseCase;
    private final DeleteIdentificationTypeUseCase deleteUseCase;
    private final IdentificationTypeMapper mapper;
    private final PaginationWebMapper paginationMapper;

    @Operation(
            summary = "Listar todos los tipos de identificación (paginado)",
            description = "Obtiene una lista paginada de todos los tipos de identificación del sistema. " +
                    "Soporta paginación y ordenamiento. Requiere permisos de lectura.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de tipos de identificación obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaginatedResponseDTO.class)
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
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<IdentificationTypeResponse>>> getAll(
            @Parameter(description = "Parámetros de paginación (página, tamaño, ordenamiento)")
            @ModelAttribute PaginationRequestDTO requestDto
    ) {

        PaginationCriteria criteria = paginationMapper.toDomain(requestDto);

        PaginatedResult<IdentificationType> domainResult = getUseCase.getAll(criteria);

        List<IdentificationTypeResponse> identificationTypeDtos = domainResult.getItems().stream()
                .map(mapper::toResponse)
                .toList();

        PaginatedResponseDTO<IdentificationTypeResponse> response = paginationMapper.toResponse(domainResult, identificationTypeDtos);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Obtener tipo de identificación por ID",
            description = "Recupera los detalles de un tipo de identificación específico usando su ID único. " +
                    "Requiere permisos de lectura.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Tipo de identificación encontrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = IdentificationTypeResponse.class)
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
                    responseCode = "404",
                    description = "Tipo de identificación no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IdentificationTypeResponse>> getById(
            @Parameter(description = "UUID del tipo de identificación a consultar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        IdentificationType domain = getUseCase.getById(id);
        IdentificationTypeResponse response = mapper.toResponse(domain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Listar tipos de identificación activos",
            description = "Obtiene todos los tipos de identificación que están marcados como activos. " +
                    "Utilizado principalmente en formularios de registro de clientes. Endpoint público."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de tipos de identificación activos obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = IdentificationTypeSelectResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<IdentificationTypeSelectResponse>>> getActive() {
        List<IdentificationTypeSelectResponse> response = getUseCase.getAllActive().stream()
                .map(mapper::toSelectResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Crear nuevo tipo de identificación",
            description = "Registra un nuevo tipo de identificación en el sistema (ej: Cédula de Ciudadanía, NIT, Pasaporte). " +
                    "Requiere permisos de creación.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Tipo de identificación creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = IdentificationTypeResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos o tipo de identificación ya existe",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para crear tipos de identificación",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<IdentificationTypeResponse>> create(
            @Parameter(description = "Datos del tipo de identificación a crear", required = true)
            @Valid @RequestBody CreateIdentificationTypeDTO dto
    ) {
        IdentificationType domainReq = mapper.toDomain(dto);
        IdentificationType createdDomain = createUseCase.create(domainReq);
        IdentificationTypeResponse response = mapper.toResponse(createdDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Actualizar tipo de identificación",
            description = "Modifica los datos de un tipo de identificación existente. " +
                    "Puede actualizar el nombre y el estado activo. Requiere permisos de actualización.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Tipo de identificación actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = IdentificationTypeResponse.class)
                    )
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
                    description = "Sin permisos para actualizar tipos de identificación",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Tipo de identificación no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IdentificationTypeResponse>> update(
            @Parameter(description = "UUID del tipo de identificación a actualizar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Parameter(description = "Nuevos datos del tipo de identificación", required = true)
            @Valid @RequestBody UpdateIdentificationTypeDTO dto
    ) {
        IdentificationType domainReq = mapper.toDomain(dto);
        IdentificationType updatedDomain = updateUseCase.update(id, domainReq);
        IdentificationTypeResponse response = mapper.toResponse(updatedDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Eliminar tipo de identificación",
            description = "Elimina permanentemente un tipo de identificación del sistema. " +
                    "Solo se puede eliminar si no está siendo utilizado por ningún cliente. " +
                    "Requiere permisos de eliminación.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Tipo de identificación eliminado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"success\": true, \"message\": \"Identification type deleted successfully\", \"data\": \"Identification type deleted successfully\"}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "No se puede eliminar porque está siendo utilizado por clientes",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para eliminar tipos de identificación",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Tipo de identificación no encontrado",
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
            @Parameter(description = "UUID del tipo de identificación a eliminar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        deleteUseCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Identification type deleted successfully"));
    }
}