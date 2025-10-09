package com.incede.nbfc.core.monolith.customer.domain.entity;


import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity for maintaining additional reference names for customers.
 * Examples include CRM ID, CIBIL value, or other external identifiers.
 *
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "customer_additional_reference_name", schema = "customers")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAdditionalReferenceName extends CustomerBaseEntity implements Serializable  {


    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_addi_ref_nme_id")
    private Integer customerAddiRefNmeId;

    @Column(name = "customer_ref_name", nullable = false)
    @NotNull(message = "customerRefName is required")
    private String customerRefName;

    @Column(name = "value_type")
    private String valueType;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_mandatory")
    private Boolean isMandatory;


    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tenant_id", nullable = false, referencedColumnName = "tenant_id")
    @NotNull(message = "tenantId  must not be blank")
    @ToString.Exclude
    private Tenant tenant;

}
