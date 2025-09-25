package com.incede.nbfc.core.monolith.customer.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAdditionalInfoResponseDto {

    private UUID identity;
    private String customerCode;
    private String status;

    private AdditionalInfoDto additional;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalInfoDto{


        private CustomerEmploymentDto employment;
        private CustomerReferralDto referrals;
        private CustomerProfileExtraDto profileExtra;
        private CustomerAssetDto assets;
        private AdditionalInfoCustomerDto additionalInfoCustomerDto;
        private AdditionalReferenceValueDto additionalReferenceValueDto;



    }



}

