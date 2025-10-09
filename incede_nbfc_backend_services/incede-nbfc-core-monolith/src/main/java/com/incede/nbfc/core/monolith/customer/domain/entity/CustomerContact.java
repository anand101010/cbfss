package com.incede.nbfc.core.monolith.customer.domain.entity;


import com.incede.nbfc.core.monolith.masterdata.domain.entity.ContactTypes;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CustomerContact entity for storing multiple contact methods.
 * Supports mobile, WhatsApp, landline, and email.
 *
 *
 *
 */
@Entity
@Table(name = "customer_contacts", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerContact extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="contact_id" )
    private Integer contactId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference is required")
    private Customer customer;

    @NotNull(message = "Contact type is required")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "contact_type", nullable = false, referencedColumnName = "contact_type_id")
    private ContactTypes contactType;

    @Column(name = "contact_value", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Contact value must not be blank")
    @Size(max = 100, message = "Contact value must not exceed 100 characters")
    private String contactValue;

    @Column(name = "is_active")
    @NotNull(message = "Active status must be specified")
    private Boolean isActive;

    @Column(name = "is_verified")
    @NotNull(message = "Active status must be specified")
    private Boolean isVerified;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "is_promotional_opt_out")
    @NotNull(message = "Promotional opt-out flag must be specified")
    private Boolean isPromotionalOptOut;



}
