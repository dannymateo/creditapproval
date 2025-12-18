package com.cotrafa.creditapproval.loantype.domain.port.in;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;

import java.util.List;
import java.util.UUID;

public interface GetLoanTypeUseCase {
    LoanType getById(UUID id);
    List<LoanType> getAll();
    List<LoanType> getAllActive();
}
