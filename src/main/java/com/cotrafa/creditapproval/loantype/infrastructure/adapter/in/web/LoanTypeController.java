package com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.IdentificationTypeResponse;
import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.loantype.domain.port.in.CreateLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.DeleteLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.GetLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.UpdateLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.CreateLoanTypeDTO;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.LoanTypeResponse;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.UpdateLoanTypeDTO;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.mapper.LoanTypeMapper;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loan-type")
@RequiredArgsConstructor
public class LoanTypeController {

    private final CreateLoanTypeUseCase createUseCase;
    private final UpdateLoanTypeUseCase updateUseCase;
    private final GetLoanTypeUseCase getUseCase;
    private final DeleteLoanTypeUseCase deleteUseCase;
    private final LoanTypeMapper mapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LoanTypeResponse>>> getAll() {
        List<LoanTypeResponse> list = getUseCase.getAll().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LoanTypeResponse>> getById(@PathVariable UUID id) {
        LoanType domain = getUseCase.getById(id);
        LoanTypeResponse response = mapper.toResponse(domain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LoanTypeResponse>> create(@Valid @RequestBody CreateLoanTypeDTO dto) {
        LoanType domainReq = mapper.toDomain(dto);
        LoanType createdDomain = createUseCase.create(domainReq);
        LoanTypeResponse response = mapper.toResponse(createdDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LoanTypeResponse>> update(@PathVariable UUID id, @Valid @RequestBody UpdateLoanTypeDTO dto) {
        LoanType domainReq = mapper.toDomain(dto);
        LoanType updatedDomain = updateUseCase.update(id, domainReq);
        LoanTypeResponse response = mapper.toResponse(updatedDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        deleteUseCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Loan type deleted successfully"));
    }
}
