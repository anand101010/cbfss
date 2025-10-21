package com.incede.nbfc.core.monolith.lead.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAdditionalReference;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAddress;
import com.incede.nbfc.core.monolith.lead.dto.LeadRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadAddressRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadSearchResponseDto;
import com.incede.nbfc.core.monolith.lead.repository.LeadAdditionalReferenceRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadAddressRepository;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AdditionalReferenceConfig;
import com.incede.nbfc.core.monolith.masterdata.repository.AdditionalReferenceConfigRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressProofTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.PostOfficesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LeadMapper {

    private final LeadAddressRepository leadAddressRepository;
    private final LeadAddressMapper leadAddressMapper;
    private final LeadAdditionalReferenceRepository leadAdditionalReferenceRepository;

    public Lead toEntity(LeadRequestDto dto) {
        Objects.requireNonNull(dto, "LeadRequestDto must not be null");

        Lead lead = new Lead();
        lead.setTenantId(dto.getTenantId());
        lead.setFullName(dto.getFullName());
        lead.setContactNumber(dto.getContactNumber());
        lead.setEmail(dto.getEmail());
        lead.setRemarks(dto.getRemarks());
        lead.setCreatedBy(getCreatedBy());
        return lead;
    }

    public void updateEntityFromDto(Lead lead, LeadRequestDto dto) {
        Objects.requireNonNull(lead, "Lead must not be null");
        Objects.requireNonNull(dto, "LeadRequestDto must not be null");

        lead.setTenantId(dto.getTenantId());
        lead.setFullName(dto.getFullName());
        lead.setContactNumber(dto.getContactNumber());
        lead.setEmail(dto.getEmail());
        lead.setRemarks(dto.getRemarks());
        lead.setUpdatedBy(getUpdatedBy());
    }

    public LeadResponseDto toResponseDto(Lead lead, String status) {
        List<LeadResponseDto.LeadDetails.Address> addresses = getAddressDetails(lead);
        List<LeadResponseDto.LeadDetails.DynamicReference> dynamicReferences = getDynamicReferences(lead);

        LeadResponseDto.LeadDetails details = LeadResponseDto.LeadDetails.builder()
                .fullName(lead.getFullName())
                .contactNumber(lead.getContactNumber())
                .email(lead.getEmail())
                .assignTo(lead.getAssignToUser() != null ? lead.getAssignToUser().getIdentity() : null)
                .remarks(lead.getRemarks())
                .gender(lead.getGender() != null ? lead.getGender().getIdentity() : null)
                .leadSourceIdentity(lead.getLeadSource() != null ? lead.getLeadSource().getIdentity() : null)
                .leadStageIdentity(lead.getLeadStage() != null ? lead.getLeadStage().getIdentity() : null)
                .leadStatusIdentity(lead.getLeadStatus() != null ? lead.getLeadStatus().getIdentity() : null)
                .interestedProductIdentity(lead.getProductService() != null ? lead.getProductService().getIdentity() : null)
                .address(addresses)
                .dynamicReferences(dynamicReferences) // <-- added
                .build();

        return LeadResponseDto.builder()
                .leadIdentity(lead.getIdentity())
                .leadCode(lead.getLeadCode())
                .status(status)
                .leadDetails(details)
                .build();
    }

    private List<LeadResponseDto.LeadDetails.Address> getAddressDetails(Lead lead) {
        List<LeadAddress> addresses = leadAddressRepository.findByLeadAndIsDelFalse(lead);

        return addresses.stream()
                .map(this::convertToAddress)
                .collect(Collectors.toList());
    }

    private LeadResponseDto.LeadDetails.Address convertToAddress(LeadAddress leadAddress) {
        return LeadResponseDto.LeadDetails.Address.builder()
                .addressTypeIdentity(leadAddress.getAddressType() != null ? leadAddress.getAddressType().getIdentity() : null)
                .houseNo(leadAddress.getHouseNo())
                .streetName(leadAddress.getStreetName())
                .placeName(leadAddress.getPlaceName())
                .landmark(leadAddress.getLandmark())
                .pincode(leadAddress.getPincode())
                .country(leadAddress.getCountry())
                .state(leadAddress.getState())
                .district(leadAddress.getDistrict())
                .postOfficeIdentity(leadAddress.getPostOfficeId() != null ? leadAddress.getPostOfficeId().getIdentity() : null)
                .city(leadAddress.getCity())
                .latitude(leadAddress.getLatitude() != null ? leadAddress.getLatitude().doubleValue() : null)
                .longitude(leadAddress.getLongitude() != null ? leadAddress.getLongitude().doubleValue() : null)
                .build();
    }

    private List<LeadResponseDto.LeadDetails.DynamicReference> getDynamicReferences(Lead lead) {
        List<LeadAdditionalReference> references = leadAdditionalReferenceRepository.findByLead(lead);

        return references.stream()
                .map(ref -> LeadResponseDto.LeadDetails.DynamicReference.builder()
                        .referenceConfigIdentity(ref.getReferenceConfigId() != null
                                ? ref.getReferenceConfigId().getIdentity()
                                : null)
                        .referenceFieldValue(ref.getReferenceFieldValue())
                        .build())
                .collect(Collectors.toList());
    }

    public void saveLeadAddress(Lead lead, LeadRequestDto.AddressDto addressDto, AddressTypeRepository addressTypeRepository, PostOfficesRepository postOfficeRepository, LeadAddressMapper leadAddressMapper, LeadAddressRepository leadAddressRepository, AddressProofTypeRepository addressProofTypeRepository) {
        LeadAddressRequestDto requestDto = new LeadAddressRequestDto();
        requestDto.setHouseNo(addressDto.getHouseNo());
        requestDto.setStreetName(addressDto.getStreetName());
        requestDto.setPlaceName(addressDto.getPlaceName());
        requestDto.setPincode(addressDto.getPincode());
        requestDto.setDigipin(addressDto.getDigipin());
        requestDto.setCountry(addressDto.getCountry());
        requestDto.setState(addressDto.getState());
        requestDto.setDistrict(addressDto.getDistrict());
        requestDto.setCity(addressDto.getCity());
        requestDto.setLandmark(addressDto.getLandmark());
        requestDto.setLatitude(addressDto.getLatitude());
        requestDto.setLongitude(addressDto.getLongitude());

        LeadAddress leadAddress = leadAddressMapper.toEntity(lead, requestDto);

        if (addressDto.getAddressTypeIdentity() != null) {
            leadAddress.setAddressType(addressTypeRepository.findByIdentity(addressDto.getAddressTypeIdentity())
                    .orElseThrow(() -> new BusinessException("Invalid address type", ErrorCodes.VALIDATION_FAILED)));
        }
        if (addressDto.getAddressProofTypeIdentity() != null) {
            leadAddress.setAddressProofType(addressProofTypeRepository.findByIdentity(addressDto.getAddressProofTypeIdentity())
                    .orElseThrow(() -> new BusinessException("Invalid address prooftype", ErrorCodes.VALIDATION_FAILED)));
        }

        if (addressDto.getPostOfficeIdentity() != null) {
            leadAddress.setPostOfficeId(postOfficeRepository.findByIdentity(addressDto.getPostOfficeIdentity())
                    .orElseThrow(() -> new BusinessException("Invalid post office", ErrorCodes.VALIDATION_FAILED)));
        }

        leadAddress.setCreatedAt(LocalDateTime.now());
        leadAddress.setIsDel(false);

        leadAddressRepository.save(leadAddress);
    }

    public LeadSearchResponseDto toSearchResponseDto(Lead lead) {
        List<LeadSearchResponseDto.Address> addresses = leadAddressRepository.findByLeadAndIsDelFalse(lead)
                .stream()
                .map(addr -> LeadSearchResponseDto.Address.builder()
                        .addressTypeIdentity(addr.getAddressType() != null ? addr.getAddressType().getIdentity() : null)
                        .houseNo(addr.getHouseNo())
                        .streetName(addr.getStreetName())
                        .placeName(addr.getPlaceName())
                        .pincode(addr.getPincode())
                        .country(addr.getCountry())
                        .state(addr.getState())
                        .district(addr.getDistrict())
                        .addressProofType(addr.getAddressProofType() != null ? addr.getAddressProofType().getIdentity() :null)
                        .postOfficeIdentity(addr.getPostOfficeId() != null ? addr.getPostOfficeId().getIdentity() : null)
                        .city(addr.getCity())
                        .landmark(addr.getLandmark())
                        .latitude(addr.getLatitude() != null ? addr.getLatitude().doubleValue() : null)
                        .longitude(addr.getLongitude() != null ? addr.getLongitude().doubleValue() : null)
                        .build())
                .toList();

        List<LeadSearchResponseDto.DynamicReference> dynamicRefs = leadAdditionalReferenceRepository.findByLead(lead)
                .stream()
                .map(ref -> LeadSearchResponseDto.DynamicReference.builder()
                        .referenceConfigIdentity(ref.getReferenceConfigId() != null ? ref.getReferenceConfigId().getIdentity() : null)
                        .referenceFieldValue(ref.getReferenceFieldValue())
                        .build())
                .toList();

        return LeadSearchResponseDto.builder()
                .leadIdentity(lead.getIdentity() != null ? lead.getIdentity().toString() : null)
                .leadCode(lead.getLeadCode())
                .fullName(lead.getFullName())
                .contactNumber(lead.getContactNumber())
                .email(lead.getEmail())
                .remarks(lead.getRemarks())
                .gender(lead.getGender() != null ? lead.getGender().getIdentity() : null)
                .interestedProduct(lead.getProductService() != null ? lead.getProductService().getIdentity() : null)
                .leadSource(lead.getLeadSource() != null ? lead.getLeadSource().getIdentity() : null)
                .leadStage(lead.getLeadStage() != null ? lead.getLeadStage().getIdentity() : null)
                .leadStatus(lead.getLeadStatus() != null ? lead.getLeadStatus().getIdentity() : null)
                .addresses(addresses)
                .dynamicReferences(dynamicRefs)
                .build();
    }



    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }
}
