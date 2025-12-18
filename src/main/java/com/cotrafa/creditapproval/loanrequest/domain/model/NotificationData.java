package com.cotrafa.creditapproval.loanrequest.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class NotificationData {
    private String toEmail;
    private String customerName;
    private String loanTypeName;
    private BigDecimal amount;
    private String requestId;
}
