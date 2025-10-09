package com.incede.nbfc.core.monolith.client.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class KycRequestDto {
    private String reference_id;
    private String document_type;
    @ToString.Exclude
    private String id_number;
    private String consent;
    private String consent_purpose;
    // Only for Driving License
    private String dob;

}
