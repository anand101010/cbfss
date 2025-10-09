package com.incede.nbfc.core.monolith.customer.domain.entity;

import com.incede.nbfc.core.monolith.domain.BaseEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity for maintaining customer unique sequence per tenant.

 * @version 1.0.0
 */
@Entity
@Table(name = "customer_unique_id_seq", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUniqueIdSeq  extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_unique_seq_id")
    private Integer customerUniqueSeqId;

    @Column(name = "tenant_id", nullable = false)
    @NotNull(message = "Tenant id must not be null")
    @Min(value = 1, message = "Tenant id must be a positive integer")
    private Integer tenantId;

    @Column(name = "seq_value")
    private Integer seqValue;




}