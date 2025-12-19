package com.cotrafa.creditapproval.loanstatus.domain.port.out;


import com.cotrafa.creditapproval.loanstatus.domain.model.LoanStatus;

import java.util.Optional;
import java.util.UUID;

public interface LoanStatusRepositoryPort {
    Optional<LoanStatus> findByName(String name);
    Optional<LoanStatus> findById(UUID id);
}