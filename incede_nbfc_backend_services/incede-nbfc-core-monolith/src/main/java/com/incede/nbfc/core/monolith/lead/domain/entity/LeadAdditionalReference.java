package com.incede.nbfc.core.monolith.lead.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AdditionalReferenceConfig;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lead_additional_references", schema = "lead")
public class LeadAdditionalReference extends LeadBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "additional_reference_id")
    private Integer additionalReferenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false, referencedColumnName = "lead_id")
    @NotNull(message = "Lead reference must not be null")
    private Lead lead;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reference_config_id", referencedColumnName = "reference_config_id", nullable = false)
    private AdditionalReferenceConfig referenceConfigId;

    @Column(name = "reference_field_value", columnDefinition = "TEXT")
    private String referenceFieldValue;
}
