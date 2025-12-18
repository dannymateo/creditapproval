package com.cotrafa.creditapproval.loantype.application.service;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.loantype.domain.port.in.CreateLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.DeleteLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.GetLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.in.UpdateLoanTypeUseCase;
import com.cotrafa.creditapproval.loantype.domain.port.out.LoanTypeRepository;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.DatabaseConflictException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanTypeApplicationService implements
        CreateLoanTypeUseCase, UpdateLoanTypeUseCase, DeleteLoanTypeUseCase, GetLoanTypeUseCase {

    private final LoanTypeRepository repository;

    @Override
    @Transactional
    public LoanType create(LoanType loanType) {
        if (repository.existsByName(loanType.getName())) {
            throw new DatabaseConflictException("Loan type name already exists");
        }

        LoanType typeToSave = loanType.toBuilder()
                .active(true)
                .build();

        return repository.save(typeToSave);
    }

    @Override
    @Transactional
    public LoanType update(UUID id, LoanType domainRequest) {
        LoanType existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan type not found"));

        if (!existing.getName().equalsIgnoreCase(domainRequest.getName()) &&
                repository.existsByName(domainRequest.getName())) {
            throw new DatabaseConflictException("Loan type name already exists");
        }

        LoanType updatedType = existing.toBuilder()
                .name(domainRequest.getName())
                .annualRate(domainRequest.getAnnualRate())
                .automaticValidation(domainRequest.isAutomaticValidation())
                .active(domainRequest.isActive())
                .build();

        return repository.save(updatedType);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Loan type not found");
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanType getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan type not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResult<LoanType> getAll(PaginationCriteria criteria) {
        return repository.findAll(criteria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanType> getAllActive() {
        return repository.findAllActive();
    }
}