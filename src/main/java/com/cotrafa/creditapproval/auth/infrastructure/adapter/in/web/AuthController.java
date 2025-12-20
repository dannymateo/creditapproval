package com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.auth.domain.model.AuthSession;
import com.cotrafa.creditapproval.auth.domain.port.in.*;
import com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.dto.*;
import com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.mapper.AuthMapper;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import com.cotrafa.creditapproval.shared.infrastructure.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Autenticación",
        description = "Operaciones relacionadas con autenticación y gestión de sesiones de usuarios. " +
                "Incluye inicio de sesión, cierre de sesión, renovación de tokens y gestión de contraseñas."
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    private final RestorePasswordUseCase restorePasswordUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    private final AuthMapper authMapper;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario mediante email y contraseña. Retorna tokens de acceso y renovación " +
                    "junto con la información de la sesión del usuario. El access token es válido por 30 minutos " +
                    "y el refresh token por 7 días."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Inicio de sesión exitoso. Retorna los tokens JWT y datos del usuario.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Credenciales inválidas o datos de entrada incorrectos",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Usuario bloqueado por múltiples intentos fallidos de inicio de sesión",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado en el sistema",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Parameter(description = "Credenciales de inicio de sesión (email y contraseña)", required = true)
            @Valid @RequestBody LoginDTO dto
    ) {
        AuthSession session = loginUseCase.login(authMapper.toDomain(dto));

        return ResponseEntity.ok(ApiResponse.success(authMapper.toResponse(session)));
    }

    @Operation(
            summary = "Renovar token de acceso",
            description = "Genera un nuevo access token utilizando un refresh token válido. " +
                    "Este endpoint permite mantener la sesión del usuario sin necesidad de volver a autenticarse. " +
                    "El refresh token debe estar activo y no haber expirado (válido por 7 días desde su emisión)."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Token renovado exitosamente. Retorna nuevos tokens JWT y datos del usuario.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Refresh token inválido o mal formado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Refresh token expirado o sesión invalidada",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Usuario asociado al token no encontrado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @Parameter(description = "Refresh token emitido durante el inicio de sesión", required = true)
            @Valid @RequestBody RefreshTokenDTO dto
    ) {
        AuthSession session = refreshTokenUseCase.refreshToken(dto.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success(authMapper.toResponse(session)));
    }

    @Operation(
            summary = "Cerrar sesión",
            description = "Invalida la sesión actual del usuario autenticado. " +
                    "Requiere un access token válido en el header Authorization. " +
                    "Una vez ejecutado, tanto el access token como el refresh token asociados quedarán invalidados " +
                    "y no podrán ser utilizados nuevamente.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Cierre de sesión exitoso. La sesión ha sido invalidada correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"success\": true, \"message\": \"Logged out successfully\", \"data\": \"Logged out successfully\"}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Token de autorización inválido, expirado o no proporcionado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor durante el proceso de cierre de sesión",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                UUID userId = jwtUtil.getUserId(token);
                UUID sessionId = jwtUtil.getSessionId(token);

                logoutUseCase.logout(userId, sessionId);
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(ApiResponse.error("An error occurred while trying to log out.", 500));
            }
        }
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }

    @Operation(
            summary = "Solicitar restauración de contraseña",
            description = "Inicia el proceso de recuperación de contraseña enviando un código OTP (One-Time Password) " +
                    "al email del usuario. Por razones de seguridad, la respuesta siempre será exitosa " +
                    "independientemente de si el email existe o no en el sistema, para evitar la enumeración de usuarios. " +
                    "El código OTP tiene una validez limitada y solo puede ser utilizado una vez."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Solicitud procesada correctamente. Si el email existe, se ha enviado un código OTP.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"success\": true, \"message\": \"If the email exists, an OTP code has been sent.\", \"data\": \"If the email exists, an OTP code has been sent.\"}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Email inválido o mal formado",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor o error al enviar el email",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/restore-password")
    public ResponseEntity<ApiResponse<String>> restorePassword(
            @Parameter(description = "Email del usuario que solicita la recuperación de contraseña", required = true)
            @Valid @RequestBody RestorePasswordDTO request
    ) {
        restorePasswordUseCase.restorePassword(request.getEmail());

        return ResponseEntity.ok(ApiResponse.success("If the email exists, an OTP code has been sent."));
    }

    @Operation(
            summary = "Cambiar contraseña con código OTP",
            description = "Completa el proceso de recuperación de contraseña utilizando el código OTP enviado por email. " +
                    "Requiere el email del usuario, el código OTP válido y la nueva contraseña. " +
                    "El código OTP debe estar activo y no haber sido utilizado previamente. " +
                    "Una vez completado, el usuario podrá iniciar sesión con la nueva contraseña."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Contraseña cambiada exitosamente. El usuario puede iniciar sesión con la nueva contraseña.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"success\": true, \"message\": \"Password changed successfully\", \"data\": \"Password changed successfully\"}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Código OTP inválido, expirado o datos de entrada incorrectos",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado o solicitud de restauración no existe",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "El código OTP ya fue utilizado previamente",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Parameter(description = "Datos necesarios para cambiar la contraseña: email, código OTP y nueva contraseña", required = true)
            @Valid @RequestBody ChangePasswordDTO dto
    ) {
        changePasswordUseCase.changePassword(authMapper.toDomain(dto));

        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }
}