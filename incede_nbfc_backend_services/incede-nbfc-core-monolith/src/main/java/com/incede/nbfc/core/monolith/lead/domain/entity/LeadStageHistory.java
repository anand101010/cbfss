package com.incede.nbfc.core.monolith.lead.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadStage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "lead_stage_history", schema = "lead")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadStageHistory extends LeadBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false, referencedColumnName = "lead_id")
    @NotNull(message = "Lead reference must not be null")
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false, referencedColumnName = "lead_stage_id")
    @NotNull(message = "Stage reference must not be null")
    private LeadStage stage;


    @NotNull(message = "Changed by user reference must not be null")
    private Integer changedByUser;

    @Column(name = "stage_date", nullable = false)
    @NotNull(message = "Stage date must not be null")
    private LocalDate stageDate;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

}
