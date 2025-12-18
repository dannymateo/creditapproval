package com.cotrafa.creditapproval.customer.domain.port.in;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import java.util.UUID;

public interface GetCustomerUseCase {
    Customer getByUserId(UUID userId);
}