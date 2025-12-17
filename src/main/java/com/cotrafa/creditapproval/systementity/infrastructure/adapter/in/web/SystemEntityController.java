package com.cotrafa.creditapproval.systementity.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import com.cotrafa.creditapproval.systementity.infrastructure.adapter.in.web.dto.SystemEntityResponse;
import com.cotrafa.creditapproval.systementity.domain.model.SystemEntity;
import com.cotrafa.creditapproval.systementity.domain.port.in.GetSystemEntitiesUseCase;
import com.cotrafa.creditapproval.systementity.infrastructure.adapter.in.web.mapper.SystemEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/system-entities")
@RequiredArgsConstructor
public class SystemEntityController {

    private final GetSystemEntitiesUseCase getAllUseCase;
    private final SystemEntityMapper mapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SystemEntityResponse>>> getAll() {

        List<SystemEntityResponse> list = getAllUseCase.getAll().stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(list));
    }
}