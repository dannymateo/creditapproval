package com.cotrafa.creditapproval.customer.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Test: Customer Domain Validation")
class CustomerValidationTest {

    @Nested
    @DisplayName("Salary Limit Rules (Requirement 1)")
    class SalaryRules {

        @Test
        @DisplayName("Should fail when salary exceeds 15,000,000 threshold")
        void shouldFailWhenSalaryIsTooHigh() {
            // GIVEN: A salary of 16M (Invalid)
            BigDecimal invalidSalary = new BigDecimal("16000000");

            Customer customer = Customer.builder()
                    .firstName("Juan")
                    .lastName("Perez")
                    .email("juan@test.com")
                    .baseSalary(invalidSalary)
                    .build();

            // WHEN / THEN: Validating integrity should throw exception
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    customer::validateIntegrity,
                    "Expected validateIntegrity() to throw, but it didn't"
            );

            assertEquals("Salary must be between 0 and 15,000,000", exception.getMessage());
        }

        @Test
        @DisplayName("Should succeed when salary is exactly 15,000,000")
        void shouldSucceedWhenSalaryIsAtLimit() {
            // GIVEN: Salary exactly at the limit
            BigDecimal edgeSalary = new BigDecimal("15000000");

            Customer customer = Customer.builder()
                    .baseSalary(edgeSalary)
                    .build();

            // WHEN / THEN: Should not throw any exception
            assertDoesNotThrow(customer::validateIntegrity);
        }

        @Test
        @DisplayName("Should fail when salary is negative")
        void shouldFailWhenSalaryIsNegative() {
            BigDecimal negativeSalary = new BigDecimal("-1");

            Customer customer = Customer.builder()
                    .baseSalary(negativeSalary)
                    .build();

            assertThrows(IllegalArgumentException.class, customer::validateIntegrity);
        }
    }
}