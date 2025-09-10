package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "canvassed_types", schema = "master_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanvassedTypes extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "canvassed_type_id")
    private Integer CanvassedTypeId;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}
