package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "customer_employment", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEmployment extends CustomerBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employment_id")
    private Integer employmentId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference is required")
    private Customer customer;

    @Column(name = "occupation_id")
    @Min(value = 1, message = "Occupation ID must be a positive Integer")
    private Integer occupationId;

    @Column(name = "designation_id")
    @Min(value = 1, message = "designationId must be a positive Integer")
    private Integer designationId;

    @Column(name = "employer", length = 100)
    @Size(max = 100, message = "Employer name must not exceed 100 characters")
    private String employer;

    @Column(name = "income_source_id")
    @Min(value=1,message ="IncomeSource Id must be a positive Integer" )
    private Integer incomeSourceId;

    @Column(name = "monthly_salary", precision = 15, scale = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "Monthly salary must be non-negative")
    private BigDecimal monthlySalary;

    @Column(name = "annual_income", precision = 15, scale = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "Annual income must be non-negative")
    private BigDecimal annualIncome;

    @Column(name = "valid_from")
    @PastOrPresent(message = "Valid from must be in the past or present")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    @FutureOrPresent(message = "Valid to must be today or a future date")
    private LocalDate validTo;

    @Column(name = "document_ref_id")
    private Integer documentRefId;

}