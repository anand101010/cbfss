package com.incede.nbfc.core.monolith.lead.dto;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LeadAssignmentSearchResponseDto  {

    private UUID leadIdentity;
    private String leadCode;
    private String fullName;
    private UUID gender;
    private String contactNumber;
    private String email;
    private UUID interestedProduct;
    private UUID leadSource;
    private UUID leadStage;
    private UUID leadStatus;
    private UUID assignToUser;



}
