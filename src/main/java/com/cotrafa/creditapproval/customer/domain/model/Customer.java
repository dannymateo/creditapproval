package com.cotrafa.creditapproval.customer.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Customer {
    private UUID id;
    private UUID userId;
    private UUID identificationTypeId;
    private String firstName;
    private String lastName;
    private String identificationNumber;
    private String email;
    private BigDecimal baseSalary;

    public void validateIntegrity() {
        if (baseSalary == null || baseSalary.compareTo(BigDecimal.ZERO) < 0
                || baseSalary.compareTo(new BigDecimal("15000000")) > 0) {
            throw new IllegalArgumentException("Salary must be between 0 and 15,000,000");
        }
    }
}