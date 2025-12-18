package com.cotrafa.creditapproval.customer.domain.port.out;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(UUID id);
    Optional<Customer> findByUserId(UUID id);
    boolean existsByIdentification(UUID typeId, String number);
}