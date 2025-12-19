package com.cotrafa.creditapproval.loan.domain.port.out;

import com.cotrafa.creditapproval.loan.domain.model.Loan;
import com.cotrafa.creditapproval.loan.domain.model.LoanReport;
import com.cotrafa.creditapproval.loan.domain.model.LoanStatusHistory;

import java.util.UUID;

public interface LoanRepositoryPort {
    Loan save(Loan loan);
    void saveHistory(LoanStatusHistory history);
    void markPreviousStatusesAsInactive(UUID loanId);
    LoanReport getApprovedLoansSummary();
}
