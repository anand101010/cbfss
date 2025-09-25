package com.incede.nbfc.core.monolith.masterdata.mapper;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Countries;
import com.incede.nbfc.core.monolith.masterdata.dto.CountryDto;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {
    public CountryDto convertToDto(Countries countries) {

        CountryDto dto = new CountryDto();
        dto.setCountry(countries.getCountry());
        dto.setCountryId(countries.getCountryId());
        dto.setIdentity(countries.getIdentity());
        dto.setIsActive(countries.getIsActive());
        return dto;
    }
}
