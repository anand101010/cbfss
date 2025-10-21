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
@Table(name = "account_type_master", schema = "master_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountTypeMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_type_id", nullable = false, length = 20)
    private Integer accountTypeId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id", nullable = false)
    @NotNull(message = "Tenant is mandatory")
    private Tenant tenant;

    @Column(name = "account_type", nullable = false, unique = true)
    private String accountType;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;


}

