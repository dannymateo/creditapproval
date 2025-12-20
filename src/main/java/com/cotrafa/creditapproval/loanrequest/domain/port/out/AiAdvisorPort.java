package com.cotrafa.creditapproval.loanrequest.domain.port.out;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;

import java.util.List;

public interface AiAdvisorPort {
    String getLoanAdvice(LoanRequest request, Customer customer, List<LoanRequest> activeLoans, String currentStatus);}