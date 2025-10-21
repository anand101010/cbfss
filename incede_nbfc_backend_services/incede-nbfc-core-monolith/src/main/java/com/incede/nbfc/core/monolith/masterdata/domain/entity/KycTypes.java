package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "kyc_types", schema = "master_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KycTypes extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kyc_type_id")
    private Integer kycTypeId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id", nullable = false)
    @NotNull(message = "Tenant is mandatory")
    private Tenant tenant;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "display_name", nullable = false, length = 50)
    private String displayName;

    @Column(name = "description", length = 120)
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}
