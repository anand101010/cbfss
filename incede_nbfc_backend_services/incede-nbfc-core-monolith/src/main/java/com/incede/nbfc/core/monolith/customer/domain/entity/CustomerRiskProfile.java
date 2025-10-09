package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "customer_risk_profiles", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRiskProfile extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "risk_id")
    private Integer riskId;

    @NotNull(message = "Customer Id must Not be null")
    @ManyToOne(fetch= FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name="customer_id",referencedColumnName="customer_id")
    private Customer customer;

    @NotNull(message = "Assesment Type must not be null")
    @Column(name = "assesment_type", nullable = false)
    @Min(value = 1, message = "Assessment type must be a positive integer")
    private Integer assessmentType;

    @Digits(integer = 5,fraction = 2)
    @Column(name = "risk_score", precision = 5, scale = 2, nullable = false)
    @NotNull(message = "Risk score must not be null")
    private BigDecimal riskScore;

    @Column(name = "risk_category", nullable = false)
    @NotNull(message = "Risk category must not be null")
    @Min(value = 1, message = "Risk category must be a positive integer")
    private Integer riskCategory;

    @NotNull(message = "Assessment date must not be null")
    @Column(name = "assessment_date", nullable = false)
    private LocalDate assessmentDate;

    @Column(name = "risk_reason", columnDefinition = "TEXT")
    private String riskReason;

}