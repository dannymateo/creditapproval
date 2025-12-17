package com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom;

import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}