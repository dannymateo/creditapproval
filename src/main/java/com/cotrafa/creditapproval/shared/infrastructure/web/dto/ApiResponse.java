package com.cotrafa.creditapproval.shared.infrastructure.web.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String error;
    private int statusCode;

    private ApiResponse(boolean success, T data, String error, int statusCode) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.statusCode = statusCode;
    }

    // Static factory method for SUCCESS responses
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, 200);
    }

    // Static factory method for ERROR responses
    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return new ApiResponse<>(false, null, message, statusCode);
    }
}