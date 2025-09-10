package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "ifsc_codes", schema = "master_data")
@AllArgsConstructor
@NoArgsConstructor
public class IfscCodes extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ifsc_code", length = 11)
    private String ifscCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_ifsc_bank"))
    private Banks bank;

    @Column(name = "branch_name", nullable = false, length = 150)
    private String branchName;

    @Column(name = "branch_place", length = 120)
    private String branchPlace;

    @Column(name = "address_line1", length = 200)
    private String addressLine1;

    @Column(name = "address_line2", length = 200)
    private String addressLine2;

    @Column(name = "address_line3", length = 200)
    private String addressLine3;

    @Column(name = "address_line4", length = 200)
    private String addressLine4;

    @Column(name = "address_line5", length = 200)
    private String addressLine5;

    @Column(name = "micr_code", length = 9)
    private String micrCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pincode_id", foreignKey = @ForeignKey(name = "fk_ifsc_pincode"))
    private Pincodes pincode;

    @Column(name = "rbi_flag", nullable = false)
    private Boolean rbiFlag = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}
