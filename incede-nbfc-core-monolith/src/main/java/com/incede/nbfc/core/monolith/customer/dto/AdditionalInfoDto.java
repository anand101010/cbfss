package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInfoDto{


    CustomerEmploymentDto employment;
    CustomerReferralDto referrals;
    CustomerProfileExtraDto profileExtra;
    CustomerAssetDto customerAsset;
    AdditionalInfoCustomerDto customer;
    AdditionalReferenceValueDto additionalReferenceValueDto;



}