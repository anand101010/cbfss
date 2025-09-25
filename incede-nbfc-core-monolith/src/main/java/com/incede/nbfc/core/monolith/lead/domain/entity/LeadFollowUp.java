package com.incede.nbfc.core.monolith.lead.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "lead_follow_ups", schema = "lead")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadFollowUp extends leadBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_up_id")
    private Integer followUpId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false, referencedColumnName = "lead_id")
    @NotNull(message = "Lead reference must not be null")
    private Lead lead;

    @Column(name = "staff_id", nullable = false)
    @NotNull(message = "Staff ID must not be null")
    private Integer staffId;

    @Column(name = "follow_up_type_id", nullable = false)
    @JoinColumn(name = "follow_up_type_id", nullable = false, referencedColumnName = "follow_up_type_id")
    @NotNull(message = "Follow-up type ID must not be null")
    private Integer followUpTypeId;

    @Column(name = "follow_up_date", nullable = false)
    @NotNull(message = "Follow-up date must not be null")
    private LocalDate followUpDate;

    @Column(name = "next_follow_up_date")
    private LocalDate nextFollowUpDate;

    @Column(name = "follow_up_notes", columnDefinition = "TEXT")
    private String followUpNotes;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
