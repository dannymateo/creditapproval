package com.cotrafa.creditapproval.loan.infrastructure.adapter.in.web.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class LoanReportResponse {
    private final BigDecimal totalAmountApproved;
    private final Long totalLoansCount;
    private final String currency;
    private final LocalDateTime generatedAt;
}