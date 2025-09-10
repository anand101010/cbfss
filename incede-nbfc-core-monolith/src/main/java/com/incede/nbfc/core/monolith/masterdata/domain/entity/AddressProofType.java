package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "address_proof_types", schema = "master_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressProofType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_proof_type_id")
    private Integer addressProofTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_address_proof_document_type"))
    private DocumentType documentType;

    @Column(name = "code", nullable = false, length = 10, unique = true)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "concise_description", length = 150)
    private String conciseDescription;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}
