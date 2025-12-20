package com.cotrafa.creditapproval.systementity.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import com.cotrafa.creditapproval.systementity.infrastructure.adapter.in.web.dto.SystemEntityResponse;
import com.cotrafa.creditapproval.systementity.domain.port.in.GetSystemEntitiesUseCase;
import com.cotrafa.creditapproval.systementity.infrastructure.adapter.in.web.mapper.SystemEntityMapper;
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

import java.util.List;

@Tag(
        name = "Entidades del Sistema",
        description = "Consulta las entidades del sistema disponibles para la asignación de permisos. " +
                "Las entidades representan los módulos del sistema (User, Role, Customer, LoanRequest, etc.) " +
                "sobre los cuales se pueden definir permisos CRUD en los roles."
)
@RestController
@RequestMapping("/api/v1/system-entities")
@RequiredArgsConstructor
public class SystemEntityController {

    private final GetSystemEntitiesUseCase getAllUseCase;
    private final SystemEntityMapper mapper;

    @Operation(
            summary = "Listar todas las entidades del sistema",
            description = "Obtiene la lista completa de entidades del sistema ordenadas. " +
                    "Esta información se utiliza principalmente al crear o editar roles " +
                    "para definir sobre qué entidades se aplicarán los permisos. " +
                    "Requiere permisos de lectura de roles.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de entidades del sistema obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SystemEntityResponse.class)
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
    public ResponseEntity<ApiResponse<List<SystemEntityResponse>>> getAll() {

        List<SystemEntityResponse> list = getAllUseCase.getAll().stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(list));
    }
}
