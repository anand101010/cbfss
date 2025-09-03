//package com.incede.nbfc.core.monolith.masterdata.domain.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//
//import java.time.LocalDateTime;
//
///**
// * Product Category Entity for Master Data Management.
// * Represents different categories of financial products offered by the NBFC.
// *
// * @author Incede NBFC Development Team
// * @version 1.0.0
// */
//@Entity
//@Table(name = "product_categories", schema = "masterdata_schema")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ProductCategory {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "category_code", nullable = false, unique = true, length = 20)
//    private String categoryCode;
//
//    @Column(name = "category_name", nullable = false, length = 100)
//    private String categoryName;
//
//    @Column(name = "description", columnDefinition = "TEXT")
//    private String description;
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