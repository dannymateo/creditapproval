package com.cotrafa.creditapproval.loanrequest.domain.port.in;

import java.util.UUID;

public interface UpdateLoanRequestStatusUseCase {
    void updateStatus(UUID requestId, UUID statusId, String observation);}
