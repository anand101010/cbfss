package com.incede.nbfc.core.monolith.masterdata.dto;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostOfficesDto {

    private Integer postOfficeId;
    private Pincodes pincode;
    private String officeName;
    private String officeType;
    private String deliveryStatus;
    private String taluk;
    private String region;
    private String division;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private UUID identity;
}
