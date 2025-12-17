package com.cotrafa.creditapproval.customer.application.service;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.customer.domain.port.in.CreateCustomerUseCase;
import com.cotrafa.creditapproval.customer.domain.port.out.CustomerRepository;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.DatabaseConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomerApplicationService implements CreateCustomerUseCase {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer create(Customer customer) {
        if (customer.getBaseSalary().compareTo(BigDecimal.ZERO) < 0 ||
                customer.getBaseSalary().compareTo(new BigDecimal("15000000")) > 0) {
            throw new DatabaseConflictException("Salary must be between 0 and 15,000,000");
        }

        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new DatabaseConflictException("Email is already registered");
        }

        if (customerRepository.existsByIdentification(customer.getIdentificationTypeId(), customer.getIdentificationNumber())) {
            throw new DatabaseConflictException("Identification number already exists for this type");
        }

        return customerRepository.save(customer);
    }
}