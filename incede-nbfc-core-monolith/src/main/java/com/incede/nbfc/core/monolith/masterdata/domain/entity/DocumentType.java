//package com.incede.nbfc.core.monolith.masterdata.domain.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * Document Type Entity for Master Data Management.
// * Represents different types of documents required for loan processing.
// *
// * @author Incede NBFC Development Team
// * @version 1.0.0
// */
//@Entity
//@Table(name = "document_types", schema = "masterdata_schema")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class DocumentType {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "document_type_code", nullable = false, unique = true, length = 20)
//    private String documentTypeCode;
//
//    @Column(name = "document_type_name", nullable = false, length = 100)
//    private String documentTypeName;
//
//    @Column(name = "description", columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "is_mandatory", nullable = false)
//    private Boolean isMandatory;
//
//    @Column(name = "max_file_size_mb")
//    private Integer maxFileSizeMb;
//
//    @ElementCollection
//    @CollectionTable(
//        name = "document_type_extensions",
//        schema = "masterdata_schema",
//        joinColumns = @JoinColumn(name = "document_type_id")
//    )
//    @Column(name = "extension", length = 10)
//    private List<String> allowedExtensions;
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
//        if (isMandatory == null) {
//            isMandatory = false;
//        }
//        if (maxFileSizeMb == null) {
//            maxFileSizeMb = 10;
//        }
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }
//}