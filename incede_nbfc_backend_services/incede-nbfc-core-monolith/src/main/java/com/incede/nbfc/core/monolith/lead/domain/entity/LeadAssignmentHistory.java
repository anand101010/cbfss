package com.incede.nbfc.core.monolith.lead.domain.entity;

import com.incede.nbfc.core.monolith.user.domain.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "lead_assignment_history", schema = "lead")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadAssignmentHistory extends LeadBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_history_id")
    private Integer assignmentHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false, referencedColumnName = "lead_id")
    @NotNull(message = "Lead reference must not be null")
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_user_id", nullable = false)
    @NotNull(message = "Assigned-to user ID must not be null")
    private User assignedToUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by_user_id")
    private User assignedByUserId;

    @Column(name = "assigned_on", nullable = false)
    @NotNull(message = "Assigned date must not be null")
    private LocalDate assignedOn;

    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "Status must not be null")
    private String status = "ACTIVE";

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
}