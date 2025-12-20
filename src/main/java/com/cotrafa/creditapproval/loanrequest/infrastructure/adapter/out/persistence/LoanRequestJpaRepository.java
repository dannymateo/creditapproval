package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface LoanRequestJpaRepository extends JpaRepository<LoanRequestJpaEntity, UUID> {

    @Query("SELECT lr FROM LoanRequestJpaEntity lr " +
            "JOIN lr.statusHistory h " +
            "WHERE h.current = true AND UPPER(h.loanRequestStatus.name) LIKE UPPER(CONCAT('%', :statusName, '%'))")
    Page<LoanRequestJpaEntity> findByCurrentStatusName(@Param("statusName") String statusName, Pageable pageable);

    @Query(value = "CALL sp_validate_loan_request(:customerId, :loanTypeId, :amount, :termMonths)", nativeQuery = true)
    UUID callAutomaticValidationProcedure(
            @Param("customerId") UUID customerId,
            @Param("loanTypeId") UUID loanTypeId,
            @Param("amount") java.math.BigDecimal amount,
            @Param("termMonths") Integer termMonths
    );
}