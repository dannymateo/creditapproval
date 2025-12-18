package com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class LoanTypeSelectResponse {
    private UUID id;
    private String name;
    private Double annualRate;
}
