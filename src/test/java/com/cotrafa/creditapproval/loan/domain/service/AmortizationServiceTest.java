package com.cotrafa.creditapproval.loan.domain.service;

import com.cotrafa.creditapproval.loan.domain.model.LoanInstallment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Test: Amortization Domain Service")
class AmortizationServiceTest {

    private static final BigDecimal TEST_AMOUNT = new BigDecimal("1000000");
    private static final Double TEST_ANNUAL_RATE = 12.0; // 1% monthly
    private static final Integer TEST_TERMS = 12;

    @Nested
    @DisplayName("Payment Plan Generation Rules")
    class PaymentPlanRules {

        @Test
        @DisplayName("Should generate a complete plan that reaches zero balance at the last installment")
        void shouldGenerateCompletePlanAndAmortizeToZero() {
            // GIVEN: A loan request for $1,000,000 at 12% annual rate for 12 months

            // WHEN: The amortization plan is calculated
            List<LoanInstallment> resultPlan = AmortizationService.calculatePlan(TEST_AMOUNT, TEST_ANNUAL_RATE, TEST_TERMS);

            // THEN: All financial constraints must be met
            assertAll("Standard Amortization Constraints",
                    () -> assertNotNull(resultPlan, "Resulting plan should not be null"),
                    () -> assertEquals(TEST_TERMS, resultPlan.size(), "Should have exactly the requested number of installments"),

                    // Validate Financial Accuracy (First Installment)
                    () -> {
                        LoanInstallment first = resultPlan.get(0);
                        BigDecimal expectedInterest = new BigDecimal("10000.00");
                        BigDecimal expectedTotal = new BigDecimal("88848.79");

                        assertEquals(0, expectedInterest.compareTo(first.getInterestAmount()), "Initial interest must be 1% of principal");
                        assertEquals(0, expectedTotal.compareTo(first.getTotalAmount().setScale(2, RoundingMode.HALF_UP)), "Monthly fixed payment is incorrect");
                    },

                    // Validate Accounting Integrity (Last Installment)
                    () -> {
                        LoanInstallment last = resultPlan.get(resultPlan.size() - 1);
                        assertEquals(0, BigDecimal.ZERO.compareTo(last.getRemainingBalance()), "Final balance must be exactly 0.00");
                    }
            );
        }
    }
}