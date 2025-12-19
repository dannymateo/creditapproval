package com.cotrafa.creditapproval.systementity.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "system_entities",
        uniqueConstraints = @UniqueConstraint(name = "uk_system_entity_name", columnNames = "name")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemEntityJpaEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "The entity name is mandatory")
    @Size(max = 150, message = "Name cannot exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String name;

    @Size(max = 50)
    @Column(name = "cod_group", length = 50)
    private String codGroup;

    @Size(max = 100)
    @Column(name = "name_group", length = 100)
    private String nameGroup;

    @NotBlank(message = "The path is mandatory for navigation")
    @Size(max = 255)
    @Pattern(regexp = "^/.*", message = "Path must start with a forward slash '/'")
    @Column(length = 255, nullable = false)
    private String path;

    @NotBlank(message = "The display name for the client is mandatory")
    @Size(max = 150)
    @Column(name = "name_to_view_client", length = 150, nullable = false)
    private String nameToViewClient;

    @NotNull(message = "The display order cannot be null")
    @Min(value = 0, message = "Order must be a positive number or zero")
    @Column(name = "\"order\"", nullable = false)
    private Integer order;
}