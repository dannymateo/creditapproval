package com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.customer.domain.port.in.CreateCustomerUseCase;
import com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.dto.CreateCustomerDTO;
import com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.dto.CustomerResponse;
import com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.mapper.CustomerMapper;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Clientes",
        description = "Operaciones relacionadas con la gestión de clientes. " +
                "Permite el registro de nuevos clientes que posteriormente podrán solicitar créditos."
)
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CreateCustomerUseCase createUseCase;
    private final CustomerMapper mapper;

    @Operation(
            summary = "Registrar nuevo cliente",
            description = "Crea un nuevo cliente en el sistema con sus datos personales y financieros. " +
                    "El cliente quedará registrado y habilitado para solicitar créditos. " +
                    "Automáticamente se crea un usuario con las credenciales de acceso que se envían por email."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Cliente registrado exitosamente. Retorna los datos del cliente creado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos o cliente ya existe con ese email o identificación",
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
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> create(
            @Parameter(description = "Datos del cliente a registrar", required = true)
            @Valid @RequestBody CreateCustomerDTO dto
    ) {
        Customer domainReq = mapper.toDomain(dto);
        Customer createdDomain = createUseCase.create(domainReq);
        CustomerResponse response = mapper.toResponse(createdDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}