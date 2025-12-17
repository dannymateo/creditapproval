package com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto;

import java.util.UUID;

public class LoanTypeResponse {
    private UUID id;
    private String name;
    private Double annualRate;
    private boolean automaticValidation;
    private boolean active;
}
