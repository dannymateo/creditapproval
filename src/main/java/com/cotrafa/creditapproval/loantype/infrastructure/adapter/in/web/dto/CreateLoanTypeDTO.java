package com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        description = "Datos requeridos para crear un nuevo tipo de crédito. " +
                "Define el nombre, tasa de interés anual, si requiere validación automática y su estado.",
        example = "{\"name\": \"Crédito Personal\", \"annualRate\": 18.5, \"automaticValidation\": false, \"active\": true}"
)
public class CreateLoanTypeDTO {

    @Schema(
            description = "Nombre del tipo de crédito. Debe ser descriptivo y único. " +
                    "Ejemplos: Crédito Personal, Crédito de Vivienda, Crédito de Vehículo, Crédito Educativo.",
            example = "Crédito Personal",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 100
    )
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Loan type name must be between 3 and 100 characters")
    private String name;

    @Schema(
            description = "Tasa de interés anual expresada en porcentaje. " +
                    "Debe ser un valor positivo. Ejemplo: 18.5 representa 18.5% anual.",
            example = "18.5",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0"
    )
    @NotNull(message = "Annual rate is required")
    @DecimalMin(value = "0.0", message = "Annual rate cannot be negative")
    private Double annualRate;

    @Schema(
            description = "Indica si las solicitudes de este tipo de crédito se validan automáticamente. " +
                    "true = Validación automática (sin intervención humana), false = Requiere revisión manual por analista.",
            example = "false",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Automatic validation status is required")
    private Boolean automaticValidation;

    @Schema(
            description = "Estado del tipo de crédito. Define si está disponible para nuevas solicitudes. " +
                    "true = Activo (disponible), false = Inactivo (no disponible).",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Active status is required")
    private Boolean active;
}
