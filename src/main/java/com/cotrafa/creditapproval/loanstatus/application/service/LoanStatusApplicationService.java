package com.cotrafa.creditapproval.loanstatus.application.service;


import com.cotrafa.creditapproval.loanstatus.domain.model.LoanStatus;
import com.cotrafa.creditapproval.loanstatus.domain.port.in.GetLoanStatusUseCase;
import com.cotrafa.creditapproval.loanstatus.domain.port.out.LoanStatusRepositoryPort;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanStatusApplicationService implements GetLoanStatusUseCase {

    private final LoanStatusRepositoryPort repositoryPort;

    @Override
    public LoanStatus getByName(String name) {
        return repositoryPort.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Status not found: " + name));
    }

    @Override
    public LoanStatus getById(UUID id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Status not found"));
    }
}
