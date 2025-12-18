package com.cotrafa.creditapproval.loanrequeststatus.application.service;

import com.cotrafa.creditapproval.loanrequeststatus.domain.model.LoanRequestStatus;
import com.cotrafa.creditapproval.loanrequeststatus.domain.port.in.GetLoanRequestStatusUseCase;
import com.cotrafa.creditapproval.loanrequeststatus.domain.port.out.LoanRequestStatusRepositoryPort;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanRequestStatusApplicationService implements GetLoanRequestStatusUseCase {

    private final LoanRequestStatusRepositoryPort repositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<LoanRequestStatus> getAll() {
        return repositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public LoanRequestStatus getByName(String name) {
        return repositoryPort.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Request Status not found: " + name));
    }
}