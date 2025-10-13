package com.incede.nbfc.core.monolith.lead.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.FollowUpType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadStage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "lead_follow_up_history", schema = "lead")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadFollowUpHistory extends LeadBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_up_history_id")
    private Integer historyId;


    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false, referencedColumnName = "lead_id")
    @NotNull(message = "Lead reference must not be null")
    private Lead lead;

    @Column(name = "staff_id", nullable = false)
    @NotNull(message = "Staff ID must not be Null")
    private Integer staffId;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "follow_up_type_id", nullable = false, referencedColumnName = "follow_up_type_id")
    @NotNull(message = "Follow-Up Type ID must not be null")
    private FollowUpType followUpTypeId;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "lead_stage_id", nullable = false, referencedColumnName = "lead_stage_id")
    @NotNull(message = "Follow-Up Type ID must not be null")
    private LeadStage leadStage;

    @Column(name = "follow_up_date", nullable = false)
    @NotNull(message = "Follow-Up Date must not be null")
    private LocalDate followUpDate;

    @Column(name = "next_follow_up_date")
    private LocalDate nextFollowUpDate;

    @Column(name = "follow_up_notes", columnDefinition = "TEXT")
    private String followUpNotes;

    @Column(name = "change_type", nullable = false, length = 20)
    @NotNull(message = "Change type must not be null")
    private String changeType;

    @Column(name = "stage_change_remarks", nullable = false, length = 255)
    @NotNull(message = "stage_change_remarks type must not be null")
    private String stageChangeRemarks;


}
