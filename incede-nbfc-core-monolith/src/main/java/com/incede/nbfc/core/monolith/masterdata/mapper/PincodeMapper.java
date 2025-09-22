package com.incede.nbfc.core.monolith.masterdata.mapper;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;
import com.incede.nbfc.core.monolith.masterdata.dto.PincodeDto;
import org.springframework.stereotype.Component;

@Component
public class PincodeMapper {

    public PincodeDto convertToDto(Pincodes pincodes){

        PincodeDto dto = new PincodeDto();
        dto.setPincodeId(pincodes.getPincodeId());
        dto.setStateId(pincodes.getStateId());
        dto.setDistrictId(pincodes.getDistrictId());
        dto.setCityId(pincodes.getCityId());
        dto.setPincode(pincodes.getPincode());
        dto.setLatitude(pincodes.getLatitude());
        dto.setLongitude(pincodes.getLongitude());
        dto.setIdentity(pincodes.getIdentity());
        dto.setStateId(pincodes.getStateId());
        dto.setDistrictId(pincodes.getDistrictId());
        dto.setCityId(pincodes.getCityId());

        return dto;
    }
}
