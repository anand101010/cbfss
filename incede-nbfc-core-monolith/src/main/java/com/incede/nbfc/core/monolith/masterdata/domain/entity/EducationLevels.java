package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "education_levels", schema = "master_data")
@AllArgsConstructor
@NoArgsConstructor
public class EducationLevels extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_level_id")
    private Integer educationLevelId;

    @Column(name = "code", nullable = false, unique = true, length = 40)
    private String code;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;


    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;

}
