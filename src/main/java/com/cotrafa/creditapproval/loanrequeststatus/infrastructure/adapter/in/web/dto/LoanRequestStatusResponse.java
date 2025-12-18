package com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.in.web.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class LoanRequestStatusResponse {
    private UUID id;
    private String name;
}
