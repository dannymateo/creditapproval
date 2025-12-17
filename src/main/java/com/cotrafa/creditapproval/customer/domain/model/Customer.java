package com.cotrafa.creditapproval.customer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private UUID id;
    private UUID userId;
    private UUID identificationTypeId;
    private String firstName;
    private String lastName;
    private String identificationNumber;
    private String email;
    private BigDecimal baseSalary;
}