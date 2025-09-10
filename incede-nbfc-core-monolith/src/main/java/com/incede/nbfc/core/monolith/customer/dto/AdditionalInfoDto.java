package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInfoDto{
    CustomerEmploymentDto employment;
    CustomerReferralDto referrals;
    CustomerPepDto pep;
    CustomerProfileExtraDto profileExtra;
    CustomerAssetDto customerAssetDto;
}