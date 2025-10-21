package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "agentmaster", schema = "master_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentMaster extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_id")
    private Integer agentId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tenant_id", nullable = false, referencedColumnName = "tenant_id")
    private Tenant tenantId;

    @Column(name = "agent_name", nullable = false, length = 100)
    @NotBlank(message = "Agent name is required")
    private String agentName;

    @Column(name = "agent_code", nullable = false, length = 10)
    @NotBlank(message = "Agent code is required")
    private String agentCode;

    @Column(name = "gender", nullable = false, length = 10)
    @NotBlank(message = "Gender is required")
    private String gender;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Address is required")
    private String address;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "contact_number", nullable = false, length = 50)
    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @Column(name = "bank_id", nullable = false)
    @NotNull(message = "Bank ID is required")
    private Integer bankId;

    @Column(name = "branch_name", nullable = false, length = 100)
    @NotBlank(message = "Branch name is required")
    private String branchName;

    @Column(name = "account_number", nullable = false, length = 30, unique = true)
    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @Column(name = "ifsc_code", nullable = false, length = 20)
    @NotBlank(message = "IFSC code is required")
    private String ifscCode;

    @Column(name = "account_holder_name", nullable = false, length = 100)
    @NotBlank(message = "Account holder name is required")
    private String accountHolderName;

    @Column(name = "upi_id", length = 100, unique = true)
    private String upiId;

    @Column(name = "account_type", nullable = false)
    @NotNull(message = "Account type is required")
    private Integer accountType;

    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "is_active")
    @NotNull(message = "isActive flag must be set")
    private Boolean isActive = true;


    @NotNull(message = "identity is required")
    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;

}
 