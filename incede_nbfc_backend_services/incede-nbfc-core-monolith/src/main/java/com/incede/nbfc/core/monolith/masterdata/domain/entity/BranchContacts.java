package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.enums.ContactChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "branch_contacts", schema = "master_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchContacts extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_contact_id")
    private Integer branchContactId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_branch_contact_branch"))
    private Branches branch;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 16)
    private ContactChannel channel;

    @Column(name = "value", nullable = false, length = 120)
    private String value;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "remarks", length = 100)
    private String remarks;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}
