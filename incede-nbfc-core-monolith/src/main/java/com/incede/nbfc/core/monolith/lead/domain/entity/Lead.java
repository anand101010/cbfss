package com.incede.nbfc.core.monolith.lead.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leads", schema = "lead")
public class Lead extends LeadBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_id")
    private Integer leadId;

    @Column(name = "tenant_id", nullable = false)
    @NotNull(message = "Tenant ID is mandatory")
    private Integer tenantId;

    @Column(name = "lead_code", nullable = false, length = 20, unique = true)
    @NotBlank(message = "Lead code is required")
    @Size(max = 20, message = "Lead code must not exceed 20 characters")
    private String leadCode;

    @Column(name = "full_name", nullable = false, length = 100)
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gender", referencedColumnName = "gender_id", nullable = false)
    private Genders gender;

    @Column(name = "contact_number", nullable = false, length = 20)
    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @Column(name = "email", length = 100, unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lead_source_id", referencedColumnName = "lead_source_id", nullable = false)
    @NotNull(message = "Lead source is required")
    private LeadSource leadSource;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lead_stage_id", referencedColumnName = "lead_stage_id", nullable = false)
    @NotNull(message = "Lead stage is required")
    private LeadStage leadStage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lead_status_id", referencedColumnName = "lead_statuses_id", nullable = false)
    @NotNull(message = "Lead status is required")
    private LeadStatus leadStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assign_to_user_id", referencedColumnName = "user_id", nullable = false)
    @NotNull(message = "Assigned user is required")
    private User assignToUser;

    @Column(name = "current_stage_date")
    private LocalDate currentStageDate;

    @Column(name = "current_assignment_date")
    private LocalDate currentAssignmentDate;

    @Column(name = "current_follow_up_type_id")
    private Integer currentFollowUpTypeId;

    @Column(name = "current_follow_up_date")
    private LocalDate currentFollowUpDate;

    @ManyToOne
    @JoinColumn(name = "product_service_id", referencedColumnName = "product_service_id")
    private ProductService productService;

    @Column(name = "remarks")
    private String remarks;
}
