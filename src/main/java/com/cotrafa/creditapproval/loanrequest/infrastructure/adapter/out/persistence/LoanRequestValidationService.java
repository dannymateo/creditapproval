package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanRequestValidationService {

    private final EntityManager entityManager;

    /**
     * Executes the stored procedure sp_validate_loan_request to validate a loan request.
     * 
     * @param customerId Customer ID
     * @param loanTypeId Loan type ID
     * @param amount Requested amount
     * @param termMonths Term in months
     * @return UUID of the resulting status (APPROVED, REJECTED or PENDING_REVIEW)
     * @throws IllegalStateException if the procedure does not return a valid status
     */
    @Transactional
    public UUID validateLoanRequest(
            UUID customerId,
            UUID loanTypeId,
            BigDecimal amount,
            Integer termMonths
    ) {
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_validate_loan_request");
            query.registerStoredProcedureParameter("p_customer_id", UUID.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_loan_type_id", UUID.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_amount", BigDecimal.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_term_months", Integer.class, ParameterMode.IN);

            query.registerStoredProcedureParameter("p_status_id", UUID.class, ParameterMode.INOUT);

            query.setParameter("p_customer_id", customerId);
            query.setParameter("p_loan_type_id", loanTypeId);
            query.setParameter("p_amount", amount);
            query.setParameter("p_term_months", termMonths);

            query.setParameter("p_status_id", null);

            query.execute();

            UUID statusId = (UUID) query.getOutputParameterValue("p_status_id");

            if (statusId == null) {
                throw new IllegalStateException("Validation procedure did not return a valid status");
            }

            return statusId;

        } catch (Exception e) {
            throw new RuntimeException("Error validating loan request", e);
        }
    }
}

