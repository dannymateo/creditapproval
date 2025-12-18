package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateLoanRequestDTO {

    @NotNull(message = "Loan Type ID is required")
    private UUID loanTypeId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be a positive value")
    @Digits(integer = 15, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;

    @NotNull(message = "Term in months is required")
    @Min(value = 1, message = "Minimum term is 1 month")
    private Integer termMonths;
}