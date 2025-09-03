package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "customer_referrals", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReferral extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "referral_id")
    private Integer referralId;

    @NotNull(message = "Customer Id must Not be null")
    @ManyToOne(fetch= FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name="customer_id",referencedColumnName="customer_id")
    private Customer customer;

    @Column(name = "referral_source_id")
    @Min(value = 1, message = "Referral source id must be a positive integer")
    private Integer referralSourceId;

    @Column(name = "canvassed_type_id")
    @Min(value = 1, message = "Canvassed type id must be a positive integer")
    private Integer canvassedTypeId;

    @Column(name = "canvasser_staff_id")
    @Min(value = 1, message = "Canvasser staff id must be a positive integer")
    private Integer canvasserStaffId;

    @Column(name = "captured_on")
    @PastOrPresent(message = "Captured on timestamp must be in the past or present")
    private LocalDate capturedOn;

    @Size(max = 200, message = "Notes must be at most 200 characters")
    @Column(name = "notes", length = 200)
    private String notes;
}
