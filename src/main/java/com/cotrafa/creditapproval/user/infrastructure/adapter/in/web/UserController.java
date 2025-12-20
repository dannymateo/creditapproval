package com.cotrafa.creditapproval.user.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.PaginationWebMapper;
import com.cotrafa.creditapproval.user.domain.model.User;
import com.cotrafa.creditapproval.user.domain.port.in.*;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginatedResponseDTO;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginationRequestDTO;
import com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto.CreateUserDTO;
import com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto.UpdateUserDTO;
import com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto.UserResponse;
import com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.mapper.UserMapper;
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
        name = "Usuarios",
        description = "Operaciones CRUD para gestionar usuarios del sistema. " +
                "Los usuarios tienen roles asignados que determinan sus permisos. " +
                "Incluye funcionalidad de gestión de contraseñas por administradores."
)
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUseCase;
    private final UpdateUserUseCase updateUseCase;
    private final GetUserUseCase getUseCase;
    private final DeleteUserUseCase deleteUseCase;
    private final PasswordManagementUseCase passwordUseCase;
    private final UserMapper userMapper;
    private final PaginationWebMapper paginationMapper;

    @Operation(
            summary = "Listar todos los usuarios (paginado)",
            description = "Obtiene una lista paginada de todos los usuarios del sistema con sus roles asignados. " +
                    "Soporta paginación y ordenamiento. Requiere permisos de lectura de usuarios.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuarios obtenida exitosamente",
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
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<UserResponse>>> getAll(
            @Parameter(description = "Parámetros de paginación (página, tamaño, ordenamiento)")
            @ModelAttribute PaginationRequestDTO requestDto
    ) {

        PaginationCriteria criteria = paginationMapper.toDomain(requestDto);

        PaginatedResult<User> domainResult = getUseCase.getAll(criteria);

        List<UserResponse> userDtos = domainResult.getItems().stream()
                .map(userMapper::toResponse)
                .toList();

        PaginatedResponseDTO<UserResponse> response = paginationMapper.toResponse(domainResult, userDtos);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Recupera los detalles completos de un usuario específico incluyendo su rol asignado. " +
                    "Requiere permisos de lectura de usuarios.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
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
                    description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(
            @Parameter(description = "UUID del usuario a consultar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        User domain = getUseCase.getById(id);
        UserResponse response = userMapper.toResponse(domain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Crear nuevo usuario",
            description = "Registra un nuevo usuario en el sistema con un rol asignado. " +
                    "Se genera una contraseña temporal aleatoria que se envía al email del usuario. " +
                    "El usuario podrá cambiarla en su primer inicio de sesión. Requiere permisos de creación de usuarios.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Usuario creado exitosamente. Contraseña temporal enviada por email.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos o email ya registrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para crear usuarios",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Rol especificado no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Parameter(description = "Datos del usuario a crear", required = true)
            @Valid @RequestBody CreateUserDTO dto
    ) {
        User domainReq = userMapper.toDomain(dto);
        User createdDomain = createUseCase.create(domainReq);
        UserResponse response = userMapper.toResponse(createdDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Actualizar usuario",
            description = "Modifica los datos de un usuario existente incluyendo su rol y estado activo. " +
                    "No modifica la contraseña (usar endpoint de reset password). Requiere permisos de actualización de usuarios.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
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
                    description = "Sin permisos para actualizar usuarios",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Usuario o rol no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @Parameter(description = "UUID del usuario a actualizar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Parameter(description = "Nuevos datos del usuario", required = true)
            @Valid @RequestBody UpdateUserDTO dto
    ) {
        User domainReq = userMapper.toDomain(dto);
        User updatedDomain = updateUseCase.update(id, domainReq);
        UserResponse response = userMapper.toResponse(updatedDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina permanentemente un usuario del sistema. " +
                    "Solo se puede eliminar si no tiene registros asociados. Requiere permisos de eliminación de usuarios.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Usuario eliminado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"success\": true, \"message\": \"User deleted successfully\", \"data\": \"User deleted successfully\"}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "No se puede eliminar porque tiene registros asociados",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para eliminar usuarios",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
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
            @Parameter(description = "UUID del usuario a eliminar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        deleteUseCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @Operation(
            summary = "Resetear contraseña de usuario (por administrador)",
            description = "Genera una nueva contraseña temporal aleatoria para el usuario y la envía a su email. " +
                    "El usuario deberá cambiarla en su próximo inicio de sesión. " +
                    "Esta operación solo puede ser realizada por un administrador. Requiere permisos de actualización de usuarios.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Contraseña reseteada exitosamente y enviada al email del usuario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"success\": true, \"message\": \"New password generated and sent to user email\", \"data\": \"New password generated and sent to user email\"}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado o token inválido",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos para resetear contraseñas",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor o error al enviar email",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPasswordByAdmin(
            @Parameter(description = "UUID del usuario al que se le reseteará la contraseña", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        passwordUseCase.resetPasswordByAdmin(id);
        return ResponseEntity.ok(ApiResponse.success("New password generated and sent to user email"));
    }
}
