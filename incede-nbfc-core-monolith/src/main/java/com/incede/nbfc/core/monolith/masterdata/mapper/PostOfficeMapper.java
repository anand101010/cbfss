package com.incede.nbfc.core.monolith.masterdata.mapper;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.PostOffices;
import com.incede.nbfc.core.monolith.masterdata.dto.PostOfficesDto;
import org.springframework.stereotype.Component;

@Component
public class PostOfficeMapper {
    public PostOfficesDto convertToDto(PostOffices postOffices) {

        if (postOffices == null) {
            return null;
        }
        PostOfficesDto dto = new PostOfficesDto();
        dto.setPostOfficeId(postOffices.getPostOfficeId());
        dto.setOfficeName(postOffices.getOfficeName());
        dto.setOfficeType(postOffices.getOfficeType());
        dto.setDeliveryStatus(postOffices.getDeliveryStatus());
        dto.setTaluk(postOffices.getTaluk());
        dto.setRegion(postOffices.getRegion());
        dto.setDivision(postOffices.getDivision());
        dto.setLatitude(postOffices.getLatitude());
        dto.setLongitude(postOffices.getLongitude());
        dto.setIdentity(postOffices.getIdentity());
        return dto;

    }
}
