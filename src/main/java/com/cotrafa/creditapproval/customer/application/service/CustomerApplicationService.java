package com.cotrafa.creditapproval.customer.application.service;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.customer.domain.port.in.CreateCustomerUseCase;
import com.cotrafa.creditapproval.customer.domain.port.out.CustomerRepository;
import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.GetIdentificationTypeUseCase;
import com.cotrafa.creditapproval.role.domain.port.out.RoleRepositoryPort;
import com.cotrafa.creditapproval.shared.domain.constants.RoleConstants;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.BadRequestException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.DatabaseConflictException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import com.cotrafa.creditapproval.user.domain.model.User;
import com.cotrafa.creditapproval.user.domain.port.in.CreateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerApplicationService implements CreateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CreateUserUseCase createUserUseCase;
    private final GetIdentificationTypeUseCase getIdentificationTypeUseCase;
    private final RoleRepositoryPort roleRepositoryPort;

    @Override
    @Transactional
    public Customer create(Customer customer) {
        validateSalary(customer.getBaseSalary());

        IdentificationType idType = getIdentificationTypeUseCase.getById(customer.getIdentificationTypeId());
        if (!idType.isActive()) {
            throw new BadRequestException("The assigned identification type '" + idType.getName() + "' is currently inactive.");
        }

        if (customerRepository.existsByIdentification(customer.getIdentificationTypeId(), customer.getIdentificationNumber())) {
            throw new DatabaseConflictException("Identification number already exists for this type");
        }

        UUID customerRoleId = roleRepositoryPort.findByName(RoleConstants.CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Role CUSTOMER not found"))
                .getId();

        User userToCreate = User.builder()
                .email(customer.getEmail())
                .roleId(customerRoleId)
                .active(true)
                .build();

        User savedUser = createUserUseCase.create(userToCreate);

        Customer customerToSave = customer.toBuilder()
                .userId(savedUser.getId())
                .build();

        Customer savedCustomer = customerRepository.save(customerToSave);

        return savedCustomer.toBuilder()
                .email(savedUser.getEmail())
                .build();
    }

    private void validateSalary(BigDecimal salary) {
        if (salary == null || salary.compareTo(BigDecimal.ZERO) < 0 ||
                salary.compareTo(new BigDecimal("15000000")) > 0) {
            throw new BadRequestException("Salary must be between 0 and 15,000,000");
        }
    }
}