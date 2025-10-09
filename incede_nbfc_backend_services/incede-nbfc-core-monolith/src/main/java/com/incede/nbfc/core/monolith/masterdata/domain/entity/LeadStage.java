package com.incede.nbfc.core.monolith.masterdata.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "lead_stages", schema = "master_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadStage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_stage_id")
    private Integer leadStageId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    @NotNull(message = "Stage name must not be null")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}