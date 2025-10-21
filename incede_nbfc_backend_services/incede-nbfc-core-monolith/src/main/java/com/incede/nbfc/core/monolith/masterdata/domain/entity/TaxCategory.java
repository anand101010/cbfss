package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * Tax Category Entity for Master Data Management.
 *
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */

@Entity
@Table(name = "tax_categories", schema = "master_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxCategory extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tax_cat_id")
    private Integer taxCatId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id", nullable = false)
    @NotNull(message = "Tenant is mandatory")
    private Tenant tenant;

    @Column(name = "tax_cat_name", nullable = false, length = 50)
    private String taxCatName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;

}