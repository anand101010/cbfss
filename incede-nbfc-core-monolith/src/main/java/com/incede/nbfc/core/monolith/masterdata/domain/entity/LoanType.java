//package com.incede.nbfc.core.monolith.masterdata.domain.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
///**
// * Loan Type Entity for Master Data Management.
// * Represents different types of loans offered by the NBFC with their specifications.
// *
// * @author Incede NBFC Development Team
// * @version 1.0.0
// */
//@Entity
//@Table(name = "loan_types", schema = "masterdata_schema")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class LoanType {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "loan_type_code", nullable = false, unique = true, length = 20)
//    private String loanTypeCode;
//
//    @Column(name = "loan_type_name", nullable = false, length = 100)
//    private String loanTypeName;
//
//    @Column(name = "description", columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "min_amount", precision = 15, scale = 2)
//    private BigDecimal minAmount;
//
//    @Column(name = "max_amount", precision = 15, scale = 2)
//    private BigDecimal maxAmount;
//
//    @Column(name = "interest_rate_range_min", precision = 5, scale = 2)
//    private BigDecimal interestRateRangeMin;
//
//    @Column(name = "interest_rate_range_max", precision = 5, scale = 2)
//    private BigDecimal interestRateRangeMax;
//
//    @Column(name = "tenure_min_months")
//    private Integer tenureMinMonths;
//
//    @Column(name = "tenure_max_months")
//    private Integer tenureMaxMonths;
//
//    @Column(name = "is_active", nullable = false)
//    private Boolean isActive;
//
//    @Column(name = "created_at", nullable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at", nullable = false)
//    private LocalDateTime updatedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//        if (isActive == null) {
//            isActive = true;
//        }
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }
//}