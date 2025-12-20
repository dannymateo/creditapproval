package com.cotrafa.creditapproval.loantype.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LoanTypeJpaRepository extends JpaRepository<LoanTypeJpaEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);
    @Query("SELECT COUNT(lr) > 0 FROM LoanRequestJpaEntity lr WHERE lr.loanType.id = :loanTypeId")
    boolean isLoanTypeAssignedToLoanRequests(@Param("loanTypeId") UUID loanTypeId);
    List<LoanTypeJpaEntity> findAllByActiveTrue();
    Page<LoanTypeJpaEntity> findByNameContainingIgnoreCase(
            String name, Pageable pageable
    );
}
