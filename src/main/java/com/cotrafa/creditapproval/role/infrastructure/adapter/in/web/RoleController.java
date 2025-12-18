package com.cotrafa.creditapproval.role.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.role.domain.port.in.CreateRoleUseCase;
import com.cotrafa.creditapproval.role.domain.port.in.DeleteRoleUseCase;
import com.cotrafa.creditapproval.role.domain.port.in.GetRoleUseCase;
import com.cotrafa.creditapproval.role.domain.port.in.UpdateRoleUseCase;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.CreateRoleDTO;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.RoleResponse;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.RoleSelectResponse;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto.UpdateRoleDTO;
import com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.mapper.RoleMapper;
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
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final CreateRoleUseCase createUseCase;
    private final UpdateRoleUseCase updateUseCase;
    private final GetRoleUseCase getUseCase;
    private final DeleteRoleUseCase deleteUseCase;
    private final RoleMapper roleMapper;
    private final PaginationWebMapper paginationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<RoleResponse>>> getAll(
            @ModelAttribute PaginationRequestDTO requestDto) {

        PaginationCriteria criteria = paginationMapper.toDomain(requestDto);

        PaginatedResult<Role> domainResult = getUseCase.getAll(criteria);

        List<RoleResponse> roleDtos = domainResult.getItems().stream()
                .map(roleMapper::toResponse)
                .toList();

        PaginatedResponseDTO<RoleResponse> response = paginationMapper.toResponse(domainResult, roleDtos);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getById(@PathVariable UUID id) {
        Role domain = getUseCase.getById(id);
        RoleResponse response = roleMapper.toResponse(domain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<RoleSelectResponse>>> getActive() {
        List<RoleSelectResponse> response = getUseCase.getAllActive().stream()
                .map(roleMapper::toSelectResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(
            @Valid @RequestBody CreateRoleDTO createRoleDTO) {
        Role domainReq = roleMapper.toDomain(createRoleDTO);
        Role createdDomain = createUseCase.create(domainReq);
        RoleResponse response = roleMapper.toResponse(createdDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRoleDTO dto
    ) {
        Role domainReq = roleMapper.toDomain(dto);
        Role updatedDomain = updateUseCase.update(id, domainReq);
        RoleResponse response = roleMapper.toResponse(updatedDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        deleteUseCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Role deleted successfully"));
    }
}