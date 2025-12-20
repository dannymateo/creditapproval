package com.cotrafa.creditapproval.role.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.role.domain.port.in.CreateRoleUseCase;
import com.cotrafa.creditapproval.role.domain.port.in.DeleteRoleUseCase;
import com.cotrafa.creditapproval.role.domain.port.in.GetRoleUseCase;
import com.cotrafa.creditapproval.role.domain.port.in.UpdateRoleUseCase;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.CreateRoleDTO;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.RoleResponse;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.RoleSelectResponse;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.UpdateRoleDTO;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.mapper.RoleMapper;
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
        name = "Roles",
        description = "Operaciones CRUD para gestionar roles y permisos del sistema. " +
                "Los roles definen qué acciones pueden realizar los usuarios sobre cada entidad (crear, leer, actualizar, eliminar)."
)
@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {

    private final CreateRoleUseCase createUseCase;
    private final UpdateRoleUseCase updateUseCase;
    private final GetRoleUseCase getUseCase;
    private final DeleteRoleUseCase deleteUseCase;
    private final RoleMapper roleMapper;
    private final PaginationWebMapper paginationMapper;

    @Operation(
            summary = "Listar todos los roles (paginado)",
            description = "Obtiene una lista paginada de todos los roles del sistema con sus permisos. " +
                    "Soporta paginación y ordenamiento. Requiere permisos de lectura de roles.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de roles obtenida exitosamente",
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
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<RoleResponse>>> getAll(
            @Parameter(description = "Parámetros de paginación (página, tamaño, ordenamiento)")
            @ModelAttribute PaginationRequestDTO requestDto
    ) {

        PaginationCriteria criteria = paginationMapper.toDomain(requestDto);

        PaginatedResult<Role> domainResult = getUseCase.getAll(criteria);

        List<RoleResponse> roleDtos = domainResult.getItems().stream()
                .map(roleMapper::toResponse)
                .toList();

        PaginatedResponseDTO<RoleResponse> response = paginationMapper.toResponse(domainResult, roleDtos);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Obtener rol por ID",
            description = "Recupera los detalles completos de un rol específico incluyendo todos sus permisos sobre entidades del sistema. " +
                    "Requiere permisos de lectura de roles.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Rol encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleResponse.class))
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
                    description = "Rol no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getById(
            @Parameter(description = "UUID del rol a consultar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        Role domain = getUseCase.getById(id);
        RoleResponse response = roleMapper.toResponse(domain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Listar roles activos",
            description = "Obtiene todos los roles activos del sistema. " +
                    "Utilizado principalmente para asignar roles a usuarios al crearlos o actualizarlos."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de roles activos obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleSelectResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<RoleSelectResponse>>> getActive() {
        List<RoleSelectResponse> response = getUseCase.getAllActive().stream()
                .map(roleMapper::toSelectResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Crear nuevo rol",
            description = "Registra un nuevo rol en el sistema con sus permisos asociados. " +
                    "Los permisos se definen por entidad del sistema (User, Role, Customer, etc.) " +
                    "y acciones (crear, leer, actualizar, eliminar). Requiere permisos de creación de roles.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Rol creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos o rol ya existe",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para crear roles",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(
            @Parameter(description = "Datos del rol a crear con sus permisos", required = true)
            @Valid @RequestBody CreateRoleDTO createRoleDTO
    ) {
        Role domainReq = roleMapper.toDomain(createRoleDTO);
        Role createdDomain = createUseCase.create(domainReq);
        RoleResponse response = roleMapper.toResponse(createdDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Actualizar rol",
            description = "Modifica los datos de un rol existente incluyendo sus permisos. " +
                    "Permite cambiar el nombre, estado activo y la matriz completa de permisos. " +
                    "Requiere permisos de actualización de roles.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Rol actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleResponse.class))
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
                    description = "Sin permisos para actualizar roles",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Rol no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> update(
            @Parameter(description = "UUID del rol a actualizar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Parameter(description = "Nuevos datos del rol con permisos actualizados", required = true)
            @Valid @RequestBody UpdateRoleDTO dto
    ) {
        Role domainReq = roleMapper.toDomain(dto);
        Role updatedDomain = updateUseCase.update(id, domainReq);
        RoleResponse response = roleMapper.toResponse(updatedDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Eliminar rol",
            description = "Elimina permanentemente un rol del sistema. " +
                    "Solo se puede eliminar si no está siendo utilizado por ningún usuario. " +
                    "Requiere permisos de eliminación de roles.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Rol eliminado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"success\": true, \"message\": \"Role deleted successfully\", \"data\": \"Role deleted successfully\"}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "No se puede eliminar porque está siendo utilizado por usuarios",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para eliminar roles",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Rol no encontrado",
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
            @Parameter(description = "UUID del rol a eliminar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        deleteUseCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Role deleted successfully"));
    }
}
