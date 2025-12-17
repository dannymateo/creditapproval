package com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom;

import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.BusinessException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BusinessException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}