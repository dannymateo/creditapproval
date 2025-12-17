package com.cotrafa.creditapproval.loantype.domain.port.in;

import java.util.UUID;

public interface DeleteLoanTypeUseCase {
    void delete(UUID id);
}
