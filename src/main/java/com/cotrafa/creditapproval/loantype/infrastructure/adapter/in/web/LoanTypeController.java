package com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.loantype.domain.port.in.CreateLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.DeleteLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.GetLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.UpdateLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.CreateLoanTypeDTO;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.LoanTypeResponse;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.LoanTypeSelectResponse;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.UpdateLoanTypeDTO;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.mapper.LoanTypeMapper;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.PaginationWebMapper;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginatedResponseDTO;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginationRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loan-type")
@RequiredArgsConstructor
public class LoanTypeController {

    private final CreateLoanTypeUseCase createUseCase;
    private final UpdateLoanTypeUseCase updateUseCase;
    private final GetLoanTypeUseCase getUseCase;
    private final DeleteLoanTypeUseCase deleteUseCase;
    private final LoanTypeMapper mapper;
    private final PaginationWebMapper paginationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<LoanTypeResponse>>> getAll(
            @ModelAttribute PaginationRequestDTO requestDto) {

        PaginationCriteria criteria = paginationMapper.toDomain(requestDto);

        PaginatedResult<LoanType> domainResult = getUseCase.getAll(criteria);

        List<LoanTypeResponse> loanTypeDtos = domainResult.getItems().stream()
                .map(mapper::toResponse)
                .toList();

        PaginatedResponseDTO<LoanTypeResponse> response = paginationMapper.toResponse(domainResult, loanTypeDtos);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LoanTypeResponse>> getById(@PathVariable UUID id) {
        LoanType domain = getUseCase.getById(id);
        LoanTypeResponse response = mapper.toResponse(domain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<LoanTypeSelectResponse>>> getActive() {
        List<LoanTypeSelectResponse> response = getUseCase.getAllActive().stream()
                .map(mapper::toSelectResponse)
                .toList();

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
