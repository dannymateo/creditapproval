package com.cotrafa.creditapproval.loan.domain.port.in;

import com.cotrafa.creditapproval.loan.domain.model.LoanReport;

public interface GetLoanReportUseCase {
    LoanReport getApprovedLoansReport();
}