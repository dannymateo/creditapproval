package com.cotrafa.creditapproval.shared.infrastructure.web.exeption;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base class for all business logic exceptions.
 * It allows the GlobalExceptionHandler to handle all custom errors
 * in a single method by accessing the status and message dynamically.
 */
@Getter
public abstract class BusinessException extends RuntimeException {
    private final HttpStatus status;
    private final int statusCode;

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.statusCode = status.value();
    }
}
