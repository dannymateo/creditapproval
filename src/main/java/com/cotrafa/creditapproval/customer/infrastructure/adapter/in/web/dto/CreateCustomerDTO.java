package com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateCustomerDTO {

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 100, message = "First name must be between 3 and 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 100, message = "Last name must be between 3 and 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email is too long")
    private String email;

    @NotNull(message = "Identification type is required")
    private UUID identificationTypeId;

    @NotBlank(message = "Identification number is required")
    @Size(max = 20)
    @Size(min = 3, max = 20, message = "Identification number must be between 3 and 20 characters")
    private String identificationNumber;

    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.0", message = "Salary cannot be negative")
    @DecimalMax(value = "15000000.0", message = "Salary cannot exceed 15,000,000")
    private BigDecimal baseSalary;
}