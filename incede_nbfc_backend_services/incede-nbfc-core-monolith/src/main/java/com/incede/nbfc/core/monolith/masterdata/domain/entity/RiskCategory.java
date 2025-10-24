package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "risk_category", schema = "master_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskCategory extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "risk_category_id")
    private Integer riskCategoryId;

    @Column(name = "category", length = 120)
    private String category;

    @Column(name = "code", nullable = false, length = 10, unique = true)
    private String code;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}
