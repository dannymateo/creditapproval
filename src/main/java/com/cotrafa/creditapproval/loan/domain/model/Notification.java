package com.cotrafa.creditapproval.loan.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder
public class Notification {
    private final String to;
    private final String subject;
    private final String template;
    private final Map<String, Object> variables;
}