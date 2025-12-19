package com.cotrafa.creditapproval.loan.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;
import java.util.UUID;

public interface LoanJpaRepository extends JpaRepository<LoanJpaEntity, UUID> {

    @Query(value = "SELECT COALESCE(SUM(l.amount), 0) as totalAmountApproved, " +
            "COUNT(l.id) as totalLoansCount " +
            "FROM loans l " +
            "JOIN loan_statuses_history h ON l.id = h.loan_id " +
            "JOIN loan_statuses s ON h.loan_status_id = s.id " +
            "WHERE h.current = true AND s.name = :statusName", nativeQuery = true)
    Map<String, Object> getRawReportData(@Param("statusName") String statusName);
}