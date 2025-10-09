package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRiskProfileDto implements Serializable {

    private Integer assessmentType;

    private BigDecimal riskScore;

    private Integer riskCategory;

    private LocalDate assessmentDate;

}