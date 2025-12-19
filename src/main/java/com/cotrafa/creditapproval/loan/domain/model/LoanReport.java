package com.cotrafa.creditapproval.loan.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class LoanReport {
    private final BigDecimal totalAmountApproved;
    private final Long totalLoansCount;
}