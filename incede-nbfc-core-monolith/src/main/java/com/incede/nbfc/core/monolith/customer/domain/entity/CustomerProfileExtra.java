package com.incede.nbfc.core.monolith.customer.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.EducationLevels;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Purpose;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity for customer profile extra details.
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "customer_profile_extra", schema = "customers")
public class CustomerProfileExtra extends CustomerBaseEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_extra_id")
    private Integer profileExtraId;

    @ManyToOne(fetch= FetchType.LAZY,cascade = {CascadeType.MERGE})
    @JoinColumn(name="customer_Id",referencedColumnName = "customer_id")
    @NotNull(message="CustomerId must not be null")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "education_level_id", referencedColumnName = "education_level_id")
    private EducationLevels educationLevelId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "purpose_id", referencedColumnName = "purpose_id")
    private Purpose purposeId;

    @Size(max = 200, message = "Notes must be at most 200 characters")
    @Column(name = "notes", length = 200)
    private String notes;


}