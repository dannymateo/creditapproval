package com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        description = "Datos requeridos para completar el cambio de contraseña utilizando el código OTP enviado por email. " +
                "Requiere el email del usuario, el código OTP válido y la nueva contraseña que cumple con los requisitos de seguridad.",
        example = "{\"email\": \"usuario@cotrafa.com\", \"otp\": \"123456\", \"newPassword\": \"NewPassword123@\"}"
)
public class ChangePasswordDTO {

    @Schema(
            description = "Correo electrónico del usuario que solicitó el cambio de contraseña. " +
                    "Debe coincidir con el email utilizado en la solicitud de restauración.",
            example = "usuario@cotrafa.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email is too long")
    private String email;

    @Schema(
            description = "Código OTP de 6 dígitos numéricos enviado al correo electrónico del usuario. " +
                    "El código tiene validez limitada y solo puede ser utilizado una vez.",
            example = "123456",
            requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^\\d{6}$",
            minLength = 6,
            maxLength = 6
    )
    @NotBlank(message = "OTP code is required")
    @Pattern(regexp = "^\\d{6}$", message = "OTP must be exactly 6 numeric digits")
    private String otp;

    @Schema(
            description = "Nueva contraseña que debe cumplir con los requisitos de seguridad: " +
                    "entre 8 y 20 caracteres, al menos un dígito, una minúscula, una mayúscula, " +
                    "un carácter especial (@#$%^&+=!.*) y sin espacios en blanco.",
            example = "NewPassword123@",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 8,
            maxLength = 20,
            format = "password"
    )
    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!.*])(?=\\S+$).{8,20}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace"
    )
    private String newPassword;
}