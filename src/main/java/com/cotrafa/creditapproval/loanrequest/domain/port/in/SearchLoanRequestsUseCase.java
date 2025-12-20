package com.cotrafa.creditapproval.loanrequest.domain.port.in;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import java.util.List;
import java.util.UUID;

public interface SearchLoanRequestsUseCase {
    List<LoanRequest> searchByCustomerAndStatus(UUID customerId, String status);
}