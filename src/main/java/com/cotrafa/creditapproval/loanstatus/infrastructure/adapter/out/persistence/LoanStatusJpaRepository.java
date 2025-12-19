package com.cotrafa.creditapproval.loanstatus.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface LoanStatusJpaRepository extends JpaRepository<LoanStatusJpaEntity, UUID> {
    Optional<LoanStatusJpaEntity> findByName(String name);
}