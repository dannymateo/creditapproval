package com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom;

import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.BusinessException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
