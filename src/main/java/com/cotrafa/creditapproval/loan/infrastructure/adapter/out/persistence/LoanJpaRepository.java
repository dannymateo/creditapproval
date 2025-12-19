package com.cotrafa.creditapproval.loan.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface LoanJpaRepository extends JpaRepository<LoanJpaEntity, UUID> {
    Optional<LoanJpaEntity> findByLoanRequestId(UUID loanRequestId);
}