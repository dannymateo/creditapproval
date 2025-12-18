package com.cotrafa.creditapproval.systementity.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
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
public class SystemEntityJpaEntity extends Auditable{

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "cod_group", length = 50)
    private String codGroup;

    @Column(name = "name_group", length = 100)
    private String nameGroup;

    @Column(length = 255)
    private String path;

    @Column(length = 150)
    private String nameToViewClient;

    @Column(name = "\"order\"", columnDefinition = "INTEGER CHECK (\"order\" >= 0)")
    private Integer order;
}