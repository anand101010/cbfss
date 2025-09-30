package com.incede.nbfc.core.monolith.masterdata.mapper;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;
import com.incede.nbfc.core.monolith.masterdata.dto.PincodeDto;
import org.springframework.stereotype.Component;

@Component
public class PincodeMapper {

    public PincodeDto convertToDto(Pincodes pincodes) {
        PincodeDto dto = new PincodeDto();

        dto.setPincode(pincodes.getPincode());
        dto.setIdentity(pincodes.getIdentity());

        if (pincodes.getCities() != null) {
            dto.setCityName(pincodes.getCities().toString());
        }

        if (pincodes.getDistricts() != null) {
            dto.setDistrictName(pincodes.getDistricts().getDistrict());
        }

        if (pincodes.getStates() != null) {
            dto.setStateName(pincodes.getStates().getState());
        }

        return dto;
    }
}

