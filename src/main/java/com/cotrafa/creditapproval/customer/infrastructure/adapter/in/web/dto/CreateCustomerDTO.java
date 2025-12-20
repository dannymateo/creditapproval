package com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(
        description = "Datos requeridos para registrar un nuevo cliente en el sistema. " +
                "El cliente podrá posteriormente solicitar créditos una vez registrado.",
        example = "{\"firstName\": \"Juan\", \"lastName\": \"Pérez\", \"email\": \"juan.perez@email.com\", " +
                "\"identificationTypeId\": \"123e4567-e89b-12d3-a456-426614174000\", " +
                "\"identificationNumber\": \"1234567890\", \"baseSalary\": 3500000}"
)
public class CreateCustomerDTO {

    @Schema(
            description = "Nombre(s) del cliente. Debe tener entre 3 y 100 caracteres.",
            example = "Juan Carlos",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 100
    )
    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 100, message = "First name must be between 3 and 100 characters")
    private String firstName;

    @Schema(
            description = "Apellido(s) del cliente. Debe tener entre 3 y 100 caracteres.",
            example = "Pérez García",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 100
    )
    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 100, message = "Last name must be between 3 and 100 characters")
    private String lastName;

    @Schema(
            description = "Correo electrónico del cliente. Debe ser único en el sistema y será usado para el inicio de sesión. " +
                    "Se enviará un email con las credenciales de acceso.",
            example = "juan.perez@email.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email is too long")
    private String email;

    @Schema(
            description = "UUID del tipo de identificación (ej: Cédula de Ciudadanía, NIT, etc.). " +
                    "Debe ser un tipo de identificación activo en el sistema.",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid"
    )
    @NotNull(message = "Identification type is required")
    private UUID identificationTypeId;

    @Schema(
            description = "Número de identificación del cliente según el tipo seleccionado. " +
                    "Debe ser único en el sistema y tener entre 3 y 20 caracteres.",
            example = "1234567890",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 20
    )
    @NotBlank(message = "Identification number is required")
    @Size(min = 3, max = 20, message = "Identification number must be between 3 and 20 characters")
    private String identificationNumber;

    @Schema(
            description = "Salario base mensual del cliente en pesos colombianos. " +
                    "Debe ser un valor positivo y no puede exceder los 15 millones. " +
                    "Este valor se utilizará para calcular la capacidad de endeudamiento.",
            example = "3500000.00",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0",
            maximum = "15000000"
    )
    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.0", message = "Salary cannot be negative")
    @DecimalMax(value = "15000000.0", message = "Salary cannot exceed 15,000,000")
    private BigDecimal baseSalary;
}