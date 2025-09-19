package com.incede.nbfc.core.monolith.masterdata.mapper;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Cities;
import com.incede.nbfc.core.monolith.masterdata.dto.CitiesDto;
import org.springframework.stereotype.Component;

@Component
public class CitiesMapper {
    public CitiesDto convertToDto(Cities cties) {

        CitiesDto dto = new CitiesDto();
        dto.setCity(cties.getCity());
        dto.setIdentity(cties.getIdentity());
        dto.setIsActive(cties.getIsActive());
        dto.setCityId(cties.getCityId());
        return dto;

    }
}
