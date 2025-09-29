package com.incede.nbfc.core.monolith.masterdata.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenants", schema = "master_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tenant extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private Integer tenantId;

    @Column(name = "tenant_code", nullable = false, unique = true, length = 50)
    private String tenantCode;

    @Column(name = "tenant_name", nullable = false, length = 100)
    private String tenantName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive ;

    @Column(name="identity",nullable = false)
    private UUID identity;


}

