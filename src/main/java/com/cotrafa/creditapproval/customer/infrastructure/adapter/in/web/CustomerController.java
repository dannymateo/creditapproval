package com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.customer.domain.port.in.CreateCustomerUseCase;
import com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.dto.CreateCustomerDTO;
import com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.dto.CustomerResponse;
import com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.mapper.CustomerMapper;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CreateCustomerUseCase createUseCase;
    private final CustomerMapper mapper;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> create(
            @Valid @RequestBody CreateCustomerDTO dto) {
        Customer domainReq = mapper.toDomain(dto);
        Customer createdDomain = createUseCase.create(domainReq);
        CustomerResponse response = mapper.toResponse(createdDomain);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}