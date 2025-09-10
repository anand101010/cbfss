package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "asset_types", schema = "master_data")
@AllArgsConstructor
@NoArgsConstructor
public class AssetTypes extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_type_id")
    private Integer assetTypeId;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "is_active")
    private Boolean isActive = true;


    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;

}
