package com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.loanrequeststatus.domain.port.in.GetLoanRequestStatusUseCase;
import com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.in.web.dto.LoanRequestStatusResponse;
import com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.in.web.mapper.LoanRequestStatusMapper;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-request-status")
@RequiredArgsConstructor
public class LoanRequestStatusController {

    private final GetLoanRequestStatusUseCase getUseCase;
    private final LoanRequestStatusMapper mapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LoanRequestStatusResponse>>> getAll() {
        List<LoanRequestStatusResponse> response = getUseCase.getAll().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}