package com.incede.nbfc.core.monolith.lead.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadSearchResponseDto {


        private String leadIdentity;
        private String leadCode;
        private String fullName;
        private UUID gender;
        private String contactNumber;
        private String email;
        private UUID interestedProduct;
        private UUID leadSource;
        private UUID leadStage;
        private UUID leadStatus;
}
