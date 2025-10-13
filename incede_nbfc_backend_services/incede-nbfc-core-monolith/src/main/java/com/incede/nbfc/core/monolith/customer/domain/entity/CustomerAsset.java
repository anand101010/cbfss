package com.incede.nbfc.core.monolith.customer.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AssetTypes;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entity for customer asset details.
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer_assets", schema = "customers")
public class CustomerAsset extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Integer assetId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference is required")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "asset_type_id", nullable = false, referencedColumnName = "asset_type_id")
    @NotNull(message = "Asset type ID is required")
    private AssetTypes assetTypeId;

    @Column(name = "description", length = 150)
    @Size(max = 150, message = "Description must not exceed 150 characters")
    private String description;

    @Column(name = "approx_value", precision = 15, scale = 2)
    @Digits(integer = 15, fraction = 2, message = "Approximate value must be a valid monetary amount")
    private BigDecimal approxValue;

    @Column(name = "owns_asset", nullable = false)
    @NotNull(message = "Ownership flag must be specified")
    private Boolean ownsAsset ;

    @Column(name = "asset_details", length = 100)
    @Size(max = 100, message = "Asset details must not exceed 100 characters")
    private String assetDetails;

    @Column(name = "has_home_loan")
    private Boolean hasHomeLoan;

    @Column(name = "home_loan_amount", precision = 15, scale = 2)
    @Digits(integer = 15, fraction = 2, message = "Home loan amount must be a valid monetary amount")
    private BigDecimal homeLoanAmount;

    @Column(name = "home_loan_company", length = 100)
    @Size(max = 100, message = "Home loan company name must not exceed 100 characters")
    private String homeLoanCompany;

    @Column(name = "document_ref_id")
    @Min(value = 1, message = "DocumentRefId must be a positive integer")
    private Integer documentRefId;
}