package com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Datos requeridos para solicitar la restauración de contraseña. " +
                "Inicia el proceso de recuperación enviando un código OTP al correo electrónico del usuario.",
        example = "{\"email\": \"usuario@cotrafa.com\"}"
)
public class RestorePasswordDTO {

    @Schema(
            description = "Correo electrónico del usuario que desea recuperar su contraseña. " +
                    "Si el email está registrado en el sistema, se enviará un código OTP de 6 dígitos. " +
                    "Por seguridad, la respuesta será la misma independientemente de si el email existe o no.",
            example = "usuario@cotrafa.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email is too long")
    private String email;
}