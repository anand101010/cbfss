package com.incede.nbfc.core.monolith.customer.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Tenant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;/**
 * Entity representing additional reference values for a customer.
 * Each record links a customer to a reference type and its value.
 */

@Entity
@Table(name = "customer_additional_reference_value", schema = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAdditionalReferenceValue extends  CustomerBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_addi_ref_val_id")
    private Integer customerAdditionalRefValId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_addi_ref_nme_id", nullable = false, referencedColumnName = "customer_addi_ref_nme_id")
    @NotNull(message = "Reference name must not be null")
    private CustomerAdditionalReferenceName customerAdditionalReferenceName;

    @Column(name = "customer_addi_ref_value", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Reference value must not be blank")
    private String referenceValue;


}