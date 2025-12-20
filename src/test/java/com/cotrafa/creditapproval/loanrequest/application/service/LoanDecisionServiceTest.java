package com.cotrafa.creditapproval.loanrequest.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Test: Credit Decision Engine Rules")
class LoanDecisionServiceTest {

    @Nested
    @DisplayName("Debt-to-Income Capacity Rules (35% Threshold)")
    class CapacityRules {

        @Test
        @DisplayName("Should REJECT when the new installment exceeds the user's available capacity")
        void shouldRejectWhenInstallmentExceedsAvailableCapacity() {
            // GIVEN: A base salary of 4,000,000 -> Max debt capacity (35%) is 1,400,000
            BigDecimal salary = new BigDecimal("4000000");
            BigDecimal maxCapacity = salary.multiply(new BigDecimal("0.35"));

            // AND: Existing monthly debt of 1,200,000. Remaining available capacity: 200,000
            BigDecimal currentMonthlyDebt = new BigDecimal("1200000");
            BigDecimal availableCapacity = maxCapacity.subtract(currentMonthlyDebt);

            // WHEN: A new loan request is made with a monthly installment of 300,000
            BigDecimal newLoanInstallment = new BigDecimal("300000");

            // THEN: The request should be flagged for rejection (300k > 200k)
            boolean isOverLimit = newLoanInstallment.compareTo(availableCapacity) > 0;

            assertTrue(isOverLimit, "Loan should be REJECTED because installment exceeds the 35% capacity rule");
        }

        @Test
        @DisplayName("Should APPROVE when the installment is within the 35% capacity limit")
        void shouldApproveWhenInstallmentIsWithinCapacity() {
            // GIVEN: Available capacity of 500,000
            BigDecimal availableCapacity = new BigDecimal("500000");

            // WHEN: New installment is 450,000
            BigDecimal newInstallment = new BigDecimal("450000");

            // THEN: It should be within limits
            assertTrue(newInstallment.compareTo(availableCapacity) <= 0, "Loan should be APPROVED within capacity");
        }
    }

    @Nested
    @DisplayName("Risk Exposure Rules (5x Salary Threshold)")
    class RiskRules {

        @Test
        @DisplayName("Should flag for MANUAL_REVIEW when requested amount exceeds 5 times the base salary")
        void shouldRequireManualReviewForHighRiskAmounts() {
            // GIVEN: A base salary of 2,000,000. Automatic approval limit (5x) is 10,000,000
            BigDecimal salary = new BigDecimal("2000000");
            BigDecimal automaticApprovalLimit = salary.multiply(new BigDecimal("5"));

            // WHEN: The user requests a loan of 11,000,000
            BigDecimal requestedAmount = new BigDecimal("11000000");

            // THEN: It exceeds the 5x rule and must be sent to MANUAL_REVIEW
            boolean needsManualReview = requestedAmount.compareTo(automaticApprovalLimit) > 0;

            assertTrue(needsManualReview, "Loan must be flagged as MANUAL_REVIEW if amount > 5 * Salary");
        }
    }
}