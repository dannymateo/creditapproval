package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        description = "Datos requeridos para crear un nuevo tipo de identificación en el sistema. " +
                "Los tipos de identificación son utilizados para clasificar documentos de identidad de clientes.",
        example = "{\"name\": \"Cédula de Ciudadanía\", \"active\": true}"
)
public class CreateIdentificationTypeDTO {

    @Schema(
            description = "Nombre del tipo de identificación. Debe ser descriptivo y único en el sistema. " +
                    "Ejemplos: Cédula de Ciudadanía, NIT, Pasaporte, Cédula de Extranjería, Tarjeta de Identidad.",
            example = "Cédula de Ciudadanía",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 50
    )
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Identification type name must be between 3 and 50 characters")
    private String name;

    @Schema(
            description = "Estado del tipo de identificación. Define si está disponible para ser seleccionado en formularios. " +
                    "true = Activo (disponible), false = Inactivo (no disponible).",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Active status is required")
    private Boolean active;
}
