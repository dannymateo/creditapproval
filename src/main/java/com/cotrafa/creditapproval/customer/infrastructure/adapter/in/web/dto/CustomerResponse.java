package com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CustomerResponse {
    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String identificationNumber;
    private BigDecimal baseSalary;
}