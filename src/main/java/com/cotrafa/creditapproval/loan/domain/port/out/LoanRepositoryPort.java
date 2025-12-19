package com.cotrafa.creditapproval.loan.domain.port.out;

import com.cotrafa.creditapproval.loan.domain.model.Loan;
import java.util.Optional;
import java.util.UUID;

public interface LoanRepositoryPort {
    Loan save(Loan loan);
    Optional<Loan> findById(UUID id);
    Optional<Loan> findByLoanRequestId(UUID requestId);
}
