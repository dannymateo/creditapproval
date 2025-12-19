package com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "loan_request_statuses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestStatusJpaEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Status name cannot be blank")
    @Size(min = 3, max = 50, message = "Status name must be between 3 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String name;
}