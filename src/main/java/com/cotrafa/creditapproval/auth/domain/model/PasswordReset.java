package com.cotrafa.creditapproval.auth.domain.model;

public record PasswordReset(String email, String otp, String newPassword) {}
