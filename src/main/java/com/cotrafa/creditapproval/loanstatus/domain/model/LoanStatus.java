package com.cotrafa.creditapproval.loanstatus.domain.model;

import lombok.*;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class LoanStatus {
    private UUID id;
    private String name;
}