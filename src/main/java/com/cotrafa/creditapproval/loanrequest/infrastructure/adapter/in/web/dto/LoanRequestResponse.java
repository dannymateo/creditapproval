package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class LoanRequestResponse {
    private UUID id;
    private BigDecimal amount;
    private Integer termMonths;
    private String applicantName;
    private String applicantEmail;
    private BigDecimal applicantSalary;
    private String loanTypeName;
    private String status;
}