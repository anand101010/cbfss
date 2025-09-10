package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "districts", schema = "master_data")
@AllArgsConstructor
@NoArgsConstructor
public class Districts extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "district", nullable = false, unique = true, length = 100)
    private String district;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}
