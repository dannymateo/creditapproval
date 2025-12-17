package com.cotrafa.creditapproval.customer.domain.port.in;

import com.cotrafa.creditapproval.customer.domain.model.Customer;

public interface CreateCustomerUseCase {
    Customer create(Customer customer);
}
