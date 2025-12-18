package com.cotrafa.creditapproval.loantype.domain.port.out;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanTypeRepository {
    LoanType save(LoanType loanType);
    Optional<LoanType> findById(UUID id);
    List<LoanType> findAll();
    List<LoanType> findAllActive();
    boolean existsByName(String name);
    void deleteById(UUID id);
}