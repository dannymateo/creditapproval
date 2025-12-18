package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface LoanRequestStatusHistoryJpaRepository extends JpaRepository<LoanRequestStatusHistoryJpaEntity, UUID> {

    @Query("SELECT s FROM LoanRequestStatusHistoryJpaEntity s WHERE s.loanRequest.id = :requestId AND s.current = true")
    Optional<LoanRequestStatusHistoryJpaEntity> findByLoanRequestIdAndCurrentTrue(@Param("requestId") UUID requestId);

    @Modifying
    @Query("UPDATE LoanRequestStatusHistoryJpaEntity s SET s.current = false WHERE s.loanRequest.id = :requestId")
    void deactivateAllByLoanRequestId(@Param("requestId") UUID requestId);
}