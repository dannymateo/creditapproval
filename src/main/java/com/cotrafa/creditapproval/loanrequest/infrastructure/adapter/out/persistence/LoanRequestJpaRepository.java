package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanRequestJpaRepository extends JpaRepository<LoanRequestJpaEntity, UUID> {}
