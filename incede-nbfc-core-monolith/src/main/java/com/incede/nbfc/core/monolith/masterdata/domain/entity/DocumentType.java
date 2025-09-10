package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.UUID;
/**
 * Document Type Entity for Master Data Management.
* Represents different types of documents required for loan processing.
 *
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
@Entity
@Table(name = "document_types", schema = "master_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentType extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_type_id")
    private Integer documentTypeId;

    @Column(name = "code", nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;

}