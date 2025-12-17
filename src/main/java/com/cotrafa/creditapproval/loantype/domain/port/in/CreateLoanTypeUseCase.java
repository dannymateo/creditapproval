package com.cotrafa.creditapproval.loantype.domain.port.in;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;

public interface CreateLoanTypeUseCase {
    LoanType create(LoanType loanType);
}