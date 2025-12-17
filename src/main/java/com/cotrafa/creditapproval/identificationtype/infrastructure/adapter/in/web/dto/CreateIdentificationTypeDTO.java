package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateIdentificationTypeDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Identification type name must be between 3 and 50 characters")
    private String name;

    @NotNull(message = "Active status is required")
    private Boolean active;
}
