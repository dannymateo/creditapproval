package com.cotrafa.creditapproval.loan.domain.service;

import com.cotrafa.creditapproval.loan.domain.model.LoanInstallment;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AmortizationService {

    public static List<LoanInstallment> calculatePlan(BigDecimal amount, Double annualRate, Integer months) {
        List<LoanInstallment> installments = new ArrayList<>();

        // 1. Monthly interest rate (i): annual rate / 12 / 100
        double monthlyRate = (annualRate / 12) / 100;

        // 2. Fixed Installment calculation
        // Numerator: (i * (1 + i)^n)
        // Denominator: ((1 + i)^n - 1)
        double powerTerm = Math.pow(1 + monthlyRate, months);
        double numerator = monthlyRate * powerTerm;
        double denominator = powerTerm - 1;

        BigDecimal monthlyPayment = amount.multiply(BigDecimal.valueOf(numerator / denominator))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal remainingBalance = amount;
        LocalDate firstDueDate = LocalDate.now().plusMonths(1);

        for (int i = 1; i <= months; i++) {
            // Interest for the month: Interest = RemainingBalance * i
            BigDecimal interest = remainingBalance.multiply(BigDecimal.valueOf(monthlyRate))
                    .setScale(2, RoundingMode.HALF_UP);

            // Principal reduction: Principal = FixedInstallment - Interest
            BigDecimal principal = monthlyPayment.subtract(interest);

            // New balance: NewBalance = RemainingBalance - Principal
            // Adjust last installment to ensure balance reaches absolute zero
            if (i == months) {
                principal = remainingBalance;
                monthlyPayment = principal.add(interest); // Adjust installment if needed
                remainingBalance = BigDecimal.ZERO;
            } else {
                remainingBalance = remainingBalance.subtract(principal);
            }

            installments.add(LoanInstallment.builder()
                    .installmentNumber(i)
                    .principalAmount(principal)
                    .interestAmount(interest)
                    .totalAmount(monthlyPayment)
                    .remainingBalance(remainingBalance.setScale(2, RoundingMode.HALF_UP))
                    .dueDate(firstDueDate.plusMonths(i - 1))
                    .build());
        }

        return installments;
    }
}