package com.cotrafa.creditapproval.loanrequeststatus.domain.port.out;

import com.cotrafa.creditapproval.loanrequeststatus.domain.model.LoanRequestStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanRequestStatusRepositoryPort {
    List<LoanRequestStatus> findAll();
    Optional<LoanRequestStatus> findByName(String name);
    Optional<LoanRequestStatus> findById(UUID id);
}