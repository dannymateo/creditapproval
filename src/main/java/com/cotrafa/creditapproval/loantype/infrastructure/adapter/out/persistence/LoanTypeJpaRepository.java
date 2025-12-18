package com.cotrafa.creditapproval.loantype.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoanTypeJpaRepository extends JpaRepository<LoanTypeJpaEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);
    List<LoanTypeJpaEntity> findAllByActiveTrue();
}
