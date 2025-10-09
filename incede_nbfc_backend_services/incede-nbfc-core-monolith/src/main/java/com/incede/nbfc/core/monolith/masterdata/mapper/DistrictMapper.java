package com.incede.nbfc.core.monolith.masterdata.mapper;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Districts;
import com.incede.nbfc.core.monolith.masterdata.dto.CitiesDto;
import com.incede.nbfc.core.monolith.masterdata.dto.DistrictDto;
import org.springframework.stereotype.Component;

@Component
public class DistrictMapper {

    public DistrictDto convertToDto(Districts districts) {

        DistrictDto dto = new DistrictDto();
        dto.setDistrict(districts.getDistrict());
        dto.setDistrictId(districts.getDistrictId());
        dto.setIsActive(districts.getIsActive());
        dto.setIdentity(districts.getIdentity());
        return dto;
    }
}
