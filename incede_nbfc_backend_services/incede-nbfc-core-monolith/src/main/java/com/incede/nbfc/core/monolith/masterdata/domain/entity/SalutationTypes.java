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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "salutation_types", schema = "master_data")
public class SalutationTypes extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="salutation_id", nullable = false, length = 20)
    private Integer salutationTypeId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id", nullable = false)
    @NotNull(message = "Tenant is mandatory")
    private Tenant tenant;

    @Column(name = "salutation", nullable = false, unique = true)
    private String salutation;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;

}
