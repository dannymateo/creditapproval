package com.cotrafa.creditapproval.loantype.domain.model;



import lombok.*;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class LoanType {
    private UUID id;
    private String name;
    private Double annualRate;
    private boolean automaticValidation;
    private boolean active;
}