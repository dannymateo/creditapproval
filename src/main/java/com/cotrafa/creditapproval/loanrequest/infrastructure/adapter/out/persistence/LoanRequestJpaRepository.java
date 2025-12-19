package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface LoanRequestJpaRepository extends JpaRepository<LoanRequestJpaEntity, UUID> {

    @Query("SELECT lr FROM LoanRequestJpaEntity lr " +
            "JOIN lr.statusHistory h " +
            "WHERE h.current = true AND UPPER(h.loanRequestStatus.name) LIKE UPPER(CONCAT('%', :statusName, '%'))")
    Page<LoanRequestJpaEntity> findByCurrentStatusName(@Param("statusName") String statusName, Pageable pageable);

    @Procedure(procedureName = "sp_validate_loan_automatic")
    UUID callAutomaticValidationProcedure(
            @Param("p_customer_id") UUID customerId,
            @Param("p_loan_type_id") UUID loanTypeId,
            @Param("p_amount") BigDecimal amount
    );
}