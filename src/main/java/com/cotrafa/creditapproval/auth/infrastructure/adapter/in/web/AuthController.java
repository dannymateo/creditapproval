package com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.auth.domain.model.AuthSession;
import com.cotrafa.creditapproval.auth.domain.port.in.*;
import com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.dto.*;
import com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.mapper.AuthMapper;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import com.cotrafa.creditapproval.shared.infrastructure.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginDTO dto) {
        AuthSession session = loginUseCase.login(authMapper.toDomain(dto));

        return ResponseEntity.ok(ApiResponse.success(authMapper.toResponse(session)));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@Valid @RequestBody RefreshTokenDTO dto) {
        AuthSession session = refreshTokenUseCase.refreshToken(dto.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success(authMapper.toResponse(session)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
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

    @PostMapping("/restore-password")
    public ResponseEntity<ApiResponse<String>> restorePassword(@Valid @RequestBody RestorePasswordDTO request) {
        restorePasswordUseCase.restorePassword(request.getEmail());

        return ResponseEntity.ok(ApiResponse.success("If the email exists, an OTP code has been sent."));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        changePasswordUseCase.changePassword(authMapper.toDomain(dto));

        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }
}