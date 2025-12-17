package com.cotrafa.creditapproval.loantype.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanType {
    private UUID id;
    private String name;
    private Double annualRate;
    private boolean automaticValidation;
    private boolean active;
}