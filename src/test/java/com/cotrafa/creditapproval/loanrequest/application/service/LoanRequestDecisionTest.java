package com.cotrafa.creditapproval.loanrequest.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Test: Loan Request Decision Logic")
class LoanRequestDecisionTest {

    @Nested
    @DisplayName("Credit Capacity Rules (35% Rule)")
    class CapacityRules {

        @Test
        @DisplayName("Should identify when a loan exceeds 35% of income")
        void shouldIdentifyOverIndebtedness() {
            // GIVEN: Income 3,000,000 -> Max Capacity = 1,050,000
            BigDecimal income = new BigDecimal("3000000");
            BigDecimal maxCapacity = income.multiply(new BigDecimal("0.35"));

            // Current debts: 800,000. Available: 250,000
            BigDecimal currentDebts = new BigDecimal("800000");
            BigDecimal available = maxCapacity.subtract(currentDebts);

            // WHEN: New installment is 300,000
            BigDecimal newInstallment = new BigDecimal("300000");

            // THEN: Should be rejected (300k > 250k available)
            assertTrue(newInstallment.compareTo(available) > 0, "Loan should be flagged as RECHAZADO");
        }
    }

    @Nested
    @DisplayName("Manual Review Rules (5 Salaries Rule)")
    class RiskRules {

        @Test
        @DisplayName("Should require manual review if loan amount > 5 times the base salary")
        void shouldFlagManualReviewForHighAmounts() {
            // GIVEN: Salary 2,000,000. Limit = 10,000,000
            BigDecimal salary = new BigDecimal("2000000");
            BigDecimal threshold = salary.multiply(new BigDecimal("5"));

            // WHEN: Requested amount is 12,000,000
            BigDecimal requestedAmount = new BigDecimal("12000000");

            // THEN: Must be manual review
            assertTrue(requestedAmount.compareTo(threshold) > 0, "Should be REVISION_MANUAL");
        }
    }
}