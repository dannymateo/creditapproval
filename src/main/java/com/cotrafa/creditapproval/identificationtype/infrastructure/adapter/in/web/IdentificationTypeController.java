package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.CreateIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.DeleteIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.GetIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.UpdateIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.CreateIdentificationTypeDTO;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.IdentificationTypeResponse;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.IdentificationTypeSelectResponse;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.UpdateIdentificationTypeDTO;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.mapper.IdentificationTypeMapper;
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
@RequestMapping("/api/v1/identification-type")
@RequiredArgsConstructor
public class IdentificationTypeController {

    private final CreateIdentificationTypeUseCase createUseCase;
    private final UpdateIdentificationTypeUseCase updateUseCase;
    private final GetIdentificationTypeUseCase getUseCase;
    private final DeleteIdentificationTypeUseCase deleteUseCase;
    private final IdentificationTypeMapper mapper;
    private final PaginationWebMapper paginationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<IdentificationTypeResponse>>> getAll(
            @ModelAttribute PaginationRequestDTO requestDto) {

        PaginationCriteria criteria = paginationMapper.toDomain(requestDto);

        PaginatedResult<IdentificationType> domainResult = getUseCase.getAll(criteria);

        List<IdentificationTypeResponse> identificationTypeDtos = domainResult.getItems().stream()
                .map(mapper::toResponse)
                .toList();

        PaginatedResponseDTO<IdentificationTypeResponse> response = paginationMapper.toResponse(domainResult, identificationTypeDtos);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IdentificationTypeResponse>> getById(@PathVariable UUID id) {
        IdentificationType domain = getUseCase.getById(id);
        IdentificationTypeResponse response = mapper.toResponse(domain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<IdentificationTypeSelectResponse>>> getActive() {
        List<IdentificationTypeSelectResponse> response = getUseCase.getAllActive().stream()
                .map(mapper::toSelectResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<IdentificationTypeResponse>> create(
            @Valid @RequestBody CreateIdentificationTypeDTO dto) {
        IdentificationType domainReq = mapper.toDomain(dto);
        IdentificationType createdDomain = createUseCase.create(domainReq);
        IdentificationTypeResponse response = mapper.toResponse(createdDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IdentificationTypeResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateIdentificationTypeDTO dto
    ) {
        IdentificationType domainReq = mapper.toDomain(dto);
        IdentificationType updatedDomain = updateUseCase.update(id, domainReq);
        IdentificationTypeResponse response = mapper.toResponse(updatedDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        deleteUseCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Identification type deleted successfully"));
    }
}