package com.cotrafa.creditapproval.loan.domain.port.out;

import com.cotrafa.creditapproval.loan.domain.model.Loan;

public interface LoanRepositoryPort {
    Loan save(Loan loan);
}
