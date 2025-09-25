package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "additional_reference_configs", schema = "master_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalReferenceConfig extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reference_config_id")
    private Integer referenceConfigId;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "product_service_id", nullable = false)
    @NotNull(message = "Product/Service must not be null")
    private Integer productServiceId;

    @Column(name = "reference_field_name", nullable = false, length = 100)
    @NotNull(message = "Reference name must not be null")
    private String referenceFieldName;

    @Column(name = "reference_field_code", nullable = false, unique = true, length = 50)
    private String referenceFieldCode;

    @Column(name = "data_type", nullable = false, length = 20)
    @NotNull(message = "Value type must not be null")
    private String dataType;

    @Column(name = "max_length")
    private Integer maxLength;

    @Column(name = "placeholder_text", length = 200)
    private String placeholderText;

    @Column(name = "help_text")
    private String helpText;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_mandatory")
    private Boolean isMandatory = false;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}
