package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateLoanStatusDTO {

    @NotNull(message = "Status ID is required")
    private UUID statusId;

    @Size(max = 500, message = "Observation is too long (max 500 characters)")
    private String observation;
}