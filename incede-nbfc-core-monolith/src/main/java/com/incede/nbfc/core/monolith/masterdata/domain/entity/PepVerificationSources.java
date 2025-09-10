package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "pep_verification_sources", schema = "master_data")
@AllArgsConstructor
@NoArgsConstructor
public class PepVerificationSources extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "pep_verification_source_id")
        private Integer pepVerificationSourceId;

        @Column(name = "code", nullable = false, length = 30, unique = true)
        private String code;

        @Column(name = "name", nullable = false, length = 80)
        private String name;

        @Column(name = "is_active", nullable = false)
        private Boolean isActive = true;

        @Column(name = "identity", nullable = false, unique = true)
        private UUID identity;
}
