package com.cotrafa.creditapproval.loantype.domain.port.in;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;

import java.util.UUID;

public interface UpdateLoanTypeUseCase {
    LoanType update(UUID id, LoanType loanType);
}
