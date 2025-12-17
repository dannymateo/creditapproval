package com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateLoanTypeDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Loan type name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Annual rate is required")
    @DecimalMin(value = "0.0", message = "Annual rate cannot be negative")
    private Double annualRate;

    @NotNull(message = "Automatic validation status is required")
    private Boolean automaticValidation;

    @NotNull(message = "Active status is required")
    private Boolean active;
}
