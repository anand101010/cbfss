package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "document_master", schema = "master_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentMaster extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id", nullable = false)
    private Integer docId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id", nullable = false)
    @NotNull(message = "Tenant is mandatory")
    private Tenant tenant;

    @Column(name = "doc_code", nullable = false, length = 10)
    private String docCode;

    @Column(name = "doc_name", nullable = false, length = 100)
    private String docname;

    @Column(name = "is_identity_proof")
    private Boolean isIdentityProof = true;

    @Column(name = "is_address_proof")
    private Boolean isAddressProof = true;

    @Column(name = "doc_category", columnDefinition = "CHAR(1)")
    private Character docCategory = 1;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}
