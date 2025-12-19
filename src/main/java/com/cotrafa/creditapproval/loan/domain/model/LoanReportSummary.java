package com.cotrafa.creditapproval.loan.domain.model;

import java.math.BigDecimal;

public interface LoanReportSummary {
    String getLoanTypeName();
    Long getTotalLoans();
    BigDecimal getTotalApprovedAmount();
}