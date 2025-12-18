package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.CreateLoanRequestUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.UpdateLoanRequestStatusUseCase;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto.CreateLoanRequestDTO;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto.UpdateLoanStatusDTO;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.mapper.LoanRequestMapper;
import com.cotrafa.creditapproval.shared.infrastructure.security.JwtUtil;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/loan-request")
@RequiredArgsConstructor
public class LoanRequestController {

    private final CreateLoanRequestUseCase createUseCase;
    private final UpdateLoanRequestStatusUseCase updateStatusUseCase;
    private final LoanRequestMapper loanRequestMapper;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<LoanRequest>> create(
            @Valid @RequestBody CreateLoanRequestDTO dto,
            HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                UUID userId = jwtUtil.getUserId(token);

                LoanRequest domain = loanRequestMapper.toDomain(dto);

                return ResponseEntity.ok(ApiResponse.success(createUseCase.create(domain, userId)));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(ApiResponse.error("An error occurred while creating the loan request.", 500));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Authorization token is missing or invalid", 401));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateLoanStatusDTO dto) {
        updateStatusUseCase.updateStatus(id, dto.getStatusId(), dto.getObservation());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}