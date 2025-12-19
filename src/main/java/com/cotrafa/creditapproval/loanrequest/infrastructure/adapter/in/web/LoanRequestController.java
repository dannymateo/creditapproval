package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.CreateLoanRequestUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.GetLoanRequestUseCase;
import com.cotrafa.creditapproval.loanrequest.domain.port.in.UpdateLoanRequestStatusUseCase;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto.CreateLoanRequestDTO;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto.LoanRequestResponse;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto.UpdateLoanStatusDTO;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.mapper.LoanRequestMapper;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.PaginationWebMapper;
import com.cotrafa.creditapproval.shared.infrastructure.security.JwtUtil;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginatedResponseDTO;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginationRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loan-request")
@RequiredArgsConstructor
public class LoanRequestController {

    private final CreateLoanRequestUseCase createUseCase;
    private final UpdateLoanRequestStatusUseCase updateStatusUseCase;
    private final GetLoanRequestUseCase getUseCase;
    private final LoanRequestMapper loanRequestMapper;
    private final JwtUtil jwtUtil;
    private final PaginationWebMapper paginationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<LoanRequestResponse>>> getAll(
            @ModelAttribute PaginationRequestDTO requestDto) {

        PaginationCriteria criteria = paginationMapper.toDomain(requestDto);
        PaginatedResult<LoanRequest> domainResult = getUseCase.getAll(criteria);

        List<LoanRequestResponse> loanRequestDtos = domainResult.getItems().stream()
                .map(loanRequestMapper::toResponse)
                .toList();

        PaginatedResponseDTO<LoanRequestResponse> response = paginationMapper.toResponse(domainResult, loanRequestDtos);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LoanRequest>> create(
            @Valid @RequestBody CreateLoanRequestDTO dto,
            HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            UUID userId = jwtUtil.getUserId(token);

            LoanRequest domain = loanRequestMapper.toDomain(dto);

            createUseCase.create(domain, userId);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(null,201));
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