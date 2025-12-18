package com.cotrafa.creditapproval.loanrequeststatus.domain.model;


import lombok.*;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class LoanRequestStatus {
    private UUID id;
    private String name;
}