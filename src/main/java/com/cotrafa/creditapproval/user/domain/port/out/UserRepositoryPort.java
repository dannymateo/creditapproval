package com.cotrafa.creditapproval.user.domain.port.out;

import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteById(UUID id);
    PaginatedResult<User> findAll(PaginationCriteria criteria);
}