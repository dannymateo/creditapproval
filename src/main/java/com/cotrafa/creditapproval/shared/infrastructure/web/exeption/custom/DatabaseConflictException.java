package com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom;

import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.BusinessException;
import org.springframework.http.HttpStatus;

public class DatabaseConflictException extends BusinessException {
    public DatabaseConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
