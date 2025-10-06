package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.domain.entity.Nominee;
import com.incede.nbfc.core.monolith.customer.dto.NomineeAddressDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDto;
import com.incede.nbfc.core.monolith.customer.mapper.NomineeDetailsMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerAddressRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.customer.repository.NomineeRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NomineeDetailsService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;
    private final NomineeRepository nomineeRepository;
    private final AddressTypeRepository addressTypeRepository;
    private final NomineeDetailsMapper nomineeMapper;
    private final RelationshipsRepository relationshipsRepository;
    private final PostOfficesRepository postOfficesRepository;

    @Transactional
    public NomineeDetailsResponseDto createNominee(UUID customerIdentity, NomineeDetailsRequestDto dto) {
        Customer customer = findCustomer(customerIdentity);

        validateMinorNominee(dto);

        boolean isExist = nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(
                customer, dto.getFullName(), relationshipsRepository.findByIdentity(dto.getRelationship())
                        .orElseThrow(() -> new BusinessException("Relationship not found", ErrorCodes.VALIDATION_FAILED)));
        if (isExist) {
            throw new BusinessException("A nominee with the same name and relationship already exists.");
        }

        BigDecimal totalShare = calculateTotalShare(customer, dto);
        validatePercentageShare(totalShare);

        NomineeAddressDto address = resolveNomineeAddress(dto, customer);
        Nominee nominee = new Nominee();
        nominee.setIdentity(UUID.randomUUID());
        populateReferences(nominee, dto, customer, address);

        nominee.setCreatedBy(nomineeMapper.getCreatedBy());
        nominee = nomineeRepository.save(nominee);

        NomineeDto nomineeDto = nomineeMapper.toNomineeDto(nominee);
        return nomineeMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, List.of(nomineeDto));
    }

    @Transactional
    public NomineeDetailsResponseDto updateNominee(UUID customerIdentity, UUID nomineeIdentity, NomineeDetailsRequestDto dto) {
        Customer customer = findCustomer(customerIdentity);

        Nominee existingNominee = nomineeRepository.findByIdentityAndCustomer(nomineeIdentity, customer)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CommonConstants.ENTITY_NOMINEE,
                        "NomineeIdentity: " + nomineeIdentity + ", CustomerIdentity: " + customerIdentity
                ));

        validateMinorNominee(dto);

        if (!existingNominee.getFullName().equals(dto.getFullName()) ||
                !existingNominee.getRelationship().getIdentity().equals(dto.getRelationship())) {

            Relationships relationships= relationshipsRepository.findByIdentity(dto.getRelationship())
                    .orElseThrow(() -> new BusinessException("Relationship not found", ErrorCodes.VALIDATION_FAILED));
            boolean isDuplicate = nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(
                    customer, dto.getFullName(),relationships);
            if (isDuplicate) {
                throw new BusinessException("A nominee with the same name and relationship already exists.");
            }
        }

        BigDecimal totalShare = calculateTotalShareForUpdate(customer, existingNominee, dto);
        validatePercentageShare(totalShare);

        NomineeAddressDto address = resolveNomineeAddress(dto, customer);
        populateReferences(existingNominee, dto, customer, address);

        existingNominee.setUpdatedBy(nomineeMapper.getUpdatedBy());
        existingNominee = nomineeRepository.save(existingNominee);

        NomineeDto nomineeDto = nomineeMapper.toNomineeDto(existingNominee);
        return nomineeMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, List.of(nomineeDto));
    }

    @Transactional
    public void deleteNominee(UUID customerIdentity, UUID nomineeIdentity) {
        Customer customer = findCustomer(customerIdentity);

        Nominee nominee = nomineeRepository.findByIdentityAndCustomer(nomineeIdentity, customer)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_NOMINEE,
                        "NomineeIdentity: " + nomineeIdentity + ", CustomerIdentity: " + customerIdentity));

        nominee.setIsDel(true);
        nominee.setUpdatedBy(nomineeMapper.getUpdatedBy());
        nomineeRepository.save(nominee);
    }

    @Transactional(readOnly = true)
    public NomineeDetailsResponseDto getNomineesByCustomerIdentity(UUID customerIdentity) {
        Customer customer = findCustomer(customerIdentity);
        return getNomineesByCustomer(customer);
    }

    private NomineeDetailsResponseDto getNomineesByCustomer(Customer customer) {
        List<Nominee> nominees = nomineeRepository.findByCustomerIdentityAndIsDelFalse(customer.getIdentity());

        List<NomineeDto> nomineeDtos = nominees.stream()
                .map(nomineeMapper::toNomineeDto)
                .toList();

        return nomineeMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, nomineeDtos);
    }

    private Customer findCustomer(UUID customerIdentity) {
        return customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));
    }

    private void validateMinorNominee(NomineeDetailsRequestDto dto) {
        if (Boolean.TRUE.equals(dto.getIsMinor())) {
            if (dto.getGuardianName() == null || dto.getGuardianName().trim().isEmpty()) {
                throw new BusinessException("Guardian's name is required for minor nominees.");
            }
            if (dto.getGuardianDob() == null) {
                throw new BusinessException("Guardian's date of birth is required for minor nominees.");
            }
            if (dto.getGuardianEmail() == null || dto.getGuardianEmail().trim().isEmpty()) {
                throw new BusinessException("Guardian's email is required for minor nominees.");
            }
            if (dto.getGuardianContactNumber() == null || dto.getGuardianContactNumber().trim().isEmpty()) {
                throw new BusinessException("Guardian's contact number is required for minor nominees.");
            }
        }
    }

    private BigDecimal calculateTotalShare(Customer customer, NomineeDetailsRequestDto dto) {
        BigDecimal share = dto.getPercentageShare() != null ? dto.getPercentageShare() : BigDecimal.valueOf(100);
        return nomineeRepository.findByCustomerIdentityAndIsDelFalse(customer.getIdentity())
                .stream()
                .map(Nominee::getPercentageShare)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(share);
    }

    private BigDecimal calculateTotalShareForUpdate(Customer customer, Nominee existingNominee, NomineeDetailsRequestDto dto) {
        BigDecimal newShare = dto.getPercentageShare() != null ? dto.getPercentageShare() : BigDecimal.valueOf(100);
        BigDecimal totalExistingShares = nomineeRepository.findByCustomerIdentityAndIsDelFalse(customer.getIdentity())
                .stream()
                .filter(n -> !n.getNomineeId().equals(existingNominee.getNomineeId()))
                .map(Nominee::getPercentageShare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalExistingShares.add(newShare);
    }

    private void validatePercentageShare(BigDecimal totalShare) {
        if (totalShare.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new BusinessException("Total percentage share cannot exceed 100.");
        }
    }

    private NomineeAddressDto resolveNomineeAddress(NomineeDetailsRequestDto dto, Customer customer) {
        if (Boolean.TRUE.equals(dto.getIsSameAddress())) {
            var permanentAddressType = addressTypeRepository
                    .findByAddressTypeNameAndIsDelFalse(CommonConstants.ADDRESS_TYPE_PERMANENT)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS, CommonConstants.ADDRESS_TYPE_PERMANENT));

            List<CustomerAddress> customerAddresses = addressRepository
                    .findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(customer, permanentAddressType);

            if (customerAddresses.isEmpty()) {
                throw new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS,
                        "No active permanent address found for customer identity: " + customer.getIdentity());
            }

            CustomerAddress customerAddress = customerAddresses.get(0);
            log.debug("CustomerAddress: addressTypeId={}, postOfficeId={}",
                    customerAddress.getAddressType() != null ? customerAddress.getAddressType().getIdentity() : null,
                    customerAddress.getPostOffice() != null ? customerAddress.getPostOffice().getIdentity() : null);

            if (customerAddress.getAddressType() == null) {
                throw new BusinessException("Customer's permanent address has no valid address type", ErrorCodes.VALIDATION_FAILED);
            }
            if (customerAddress.getPostOffice() == null) {
                throw new BusinessException("Customer's permanent address has no valid post office", ErrorCodes.VALIDATION_FAILED);
            }

            return nomineeMapper.toNomineeAddressDtoFromCustomerAddress(customerAddress);
        } else {
            if (dto.getAddressTypeId() == null || dto.getDoorNumber() == null || dto.getAddressLine1() == null) {
                throw new BusinessException("Address must be provided when isSameAddress is false");
            }
            var addressType = addressTypeRepository.findByIdentity(dto.getAddressTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS, "AddressTypeId: " + dto.getAddressTypeId()));
            var postOffice = postOfficesRepository.findByIdentity(dto.getPostOfficeId())
                    .orElseThrow(() -> new ResourceNotFoundException( "Post Office not found"));

            return NomineeAddressDto.builder()
                    .addressTypeId(addressType.getIdentity())
                    .doorNumber(dto.getDoorNumber())
                    .addressLine1(dto.getAddressLine1())
                    .landmark(dto.getLandmark())
                    .placeName(dto.getPlaceName())
                    .city(dto.getCity())
                    .district(dto.getDistrict())
                    .state(dto.getState())
                    .country(dto.getCountry())
                    .pincode(dto.getPincode())
                    .postOfficeId(postOffice.getIdentity())
                    .latitude(dto.getLatitude())
                    .longitude(dto.getLongitude())
                    .digipin(dto.getDigipin())
                    .build();
        }
    }

    private void populateReferences(Nominee nominee, NomineeDetailsRequestDto dto, Customer customer, NomineeAddressDto address) {
        Objects.requireNonNull(dto.getRelationship(), "Relationship is required");
        nominee.setRelationship(relationshipsRepository.findByIdentity(dto.getRelationship())
                .orElseThrow(() -> new BusinessException("Invalid relationship", ErrorCodes.VALIDATION_FAILED)));

        Objects.requireNonNull(customer, "Customer is required");
        nominee.setCustomer(customer);

        log.debug("Populating Nominee: addressTypeId={}, postOfficeId={}",
                address.getAddressTypeId(), address.getPostOfficeId());

        Objects.requireNonNull(address.getAddressTypeId(), "Address type is required");
        nominee.setAddressTypeId(addressTypeRepository.findByIdentity(address.getAddressTypeId())
                .orElseThrow(() -> new BusinessException("Invalid address type", ErrorCodes.VALIDATION_FAILED)));

        Objects.requireNonNull(address.getPostOfficeId(), "Post office is required");
        nominee.setPostOfficeId(postOfficesRepository.findByIdentity(address.getPostOfficeId())
                .orElseThrow(() -> new BusinessException("Invalid post office", ErrorCodes.VALIDATION_FAILED)));

        Objects.requireNonNull(address.getCity(), "City is required");
        nominee.setCity(address.getCity());

        Objects.requireNonNull(address.getDistrict(), "District is required");
        nominee.setDistrict(address.getDistrict());

        Objects.requireNonNull(address.getState(), "State is required");
        nominee.setState(address.getState());

        Objects.requireNonNull(address.getCountry(), "Country is required");
        nominee.setCountry(address.getCountry());

        Objects.requireNonNull(address.getPincode(), "Pincode is required");
        nominee.setPincode(address.getPincode());

        nominee.setHouseNumber(address.getDoorNumber());
        nominee.setStreet(address.getAddressLine1());
        nominee.setLandmark(address.getLandmark());
        nominee.setPlaceName(address.getPlaceName());
        nominee.setLatitude(address.getLatitude());
        nominee.setLongitude(address.getLongitude());
        nominee.setDigipin(address.getDigipin());

        nominee.setFullName(dto.getFullName());
        nominee.setDob(dto.getDob());
        nominee.setContactNumber(dto.getContactNumber());
        nominee.setIsSameAddress(dto.getIsSameAddress());
        nominee.setIsMinor(dto.getIsMinor());
        nominee.setGuardianName(dto.getGuardianName());
        nominee.setGuardianEmail(dto.getGuardianEmail());
        nominee.setGuardianDob(dto.getGuardianDob());
        nominee.setGuardianContactNumber(dto.getGuardianContactNumber());
        nominee.setPercentageShare(dto.getPercentageShare() != null ? dto.getPercentageShare() : BigDecimal.valueOf(100));
    }
}