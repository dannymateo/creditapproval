package com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(
        description = "Respuesta con los datos del cliente registrado en el sistema. " +
                "Contiene la información básica del cliente y referencias a su usuario asociado.",
        example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", " +
                "\"userId\": \"987fcdeb-51a2-43d7-9012-345678901234\", " +
                "\"firstName\": \"Juan Carlos\", \"lastName\": \"Pérez García\", " +
                "\"email\": \"juan.perez@email.com\", \"identificationNumber\": \"1234567890\", " +
                "\"baseSalary\": 3500000.00}"
)
public class CustomerResponse {

    @Schema(
            description = "Identificador único del cliente en el sistema",
            example = "123e4567-e89b-12d3-a456-426614174000",
            format = "uuid"
    )
    private UUID id;

    @Schema(
            description = "Identificador único del usuario asociado al cliente. " +
                    "Este usuario se crea automáticamente durante el registro del cliente.",
            example = "987fcdeb-51a2-43d7-9012-345678901234",
            format = "uuid"
    )
    private UUID userId;

    @Schema(
            description = "Nombre(s) del cliente",
            example = "Juan Carlos"
    )
    private String firstName;

    @Schema(
            description = "Apellido(s) del cliente",
            example = "Pérez García"
    )
    private String lastName;

    @Schema(
            description = "Correo electrónico del cliente registrado",
            example = "juan.perez@email.com"
    )
    private String email;

    @Schema(
            description = "Número de identificación del cliente",
            example = "1234567890"
    )
    private String identificationNumber;

    @Schema(
            description = "Salario base mensual del cliente en pesos colombianos",
            example = "3500000.00"
    )
    private BigDecimal baseSalary;
}