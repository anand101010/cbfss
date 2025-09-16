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
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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

    /**
     *
     * @param customerIdentity
     * @param dto
     * @return
     */
    @Transactional
    public NomineeDetailsResponseDto createNominee(UUID customerIdentity, NomineeDetailsRequestDto dto) {
        Customer customer = findCustomer(customerIdentity);

        validateMinorNominee(dto);

        boolean isExist = nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(
                customer, dto.getFullName(), dto.getRelationship());
        if (isExist) {
            throw new BusinessException("A nominee with the same name and relationship already exists.");
        }

        BigDecimal totalShare = calculateTotalShare(customer, dto);
        validatePercentageShare(totalShare);

        NomineeAddressDto address = resolveNomineeAddress(dto, customer);

        Nominee nominee = nomineeMapper.toEntity(customer, dto, address);
        nominee = nomineeRepository.save(nominee);

        NomineeDto nomineeDto = nomineeMapper.toNomineeDto(nominee);
        return nomineeMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, List.of(nomineeDto));
    }

    /**
     *
     * @param customerIdentity
     * @param nomineeIdentity
     * @param dto
     * @return
     */
    @Transactional
    public NomineeDetailsResponseDto updateNominee(UUID customerIdentity, UUID nomineeIdentity, NomineeDetailsRequestDto dto) {
        Customer customer = findCustomer(customerIdentity);

        Nominee existingNominee = nomineeRepository.findByIdentityAndCustomer(nomineeIdentity, customer)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CommonConstants.ENTITY_NOMINEE,
                        "NomineeIdentity: " + nomineeIdentity + ", CustomerIdentity: " + customerIdentity
                ));

        validateMinorNominee(dto);

        BigDecimal totalShare = calculateTotalShareForUpdate(customer, existingNominee, dto);
        validatePercentageShare(totalShare);

        NomineeAddressDto address = resolveNomineeAddress(dto, customer);

        nomineeMapper.updateEntity(existingNominee, dto, address);
        existingNominee = nomineeRepository.save(existingNominee);

        NomineeDto nomineeDto = nomineeMapper.toNomineeDto(existingNominee);
        return nomineeMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, List.of(nomineeDto));
    }

    /**
     *
     * @param customerIdentity
     * @param nomineeIdentity
     */
    @Transactional
    public void deleteNominee(UUID customerIdentity, UUID nomineeIdentity) {
        Customer customer = findCustomer(customerIdentity);

        Nominee nominee = nomineeRepository.findByIdentityAndCustomer(nomineeIdentity, customer)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_NOMINEE,
                        "NomineeIdentity: " + nomineeIdentity + ", CustomerIdentity: " + customerIdentity));

        nominee.setIsDel(true);
        nomineeRepository.save(nominee);
    }

    /**
     *
     * @param customerIdentity
     * @return
     */
    @Transactional(readOnly = true)
    public NomineeDetailsResponseDto getNomineesByCustomerIdentity(String customerIdentity) {
        Customer customer = findCustomer(UUID.fromString(customerIdentity));
        return getNomineesByCustomer(customer);
    }

    /**
     *
     * @param customer
     * @return
     */
    private NomineeDetailsResponseDto getNomineesByCustomer(Customer customer) {
        List<Nominee> nominees = nomineeRepository.findByCustomerIdentityAndIsDelFalse(customer.getIdentity());

        List<NomineeDto> nomineeDtos = nominees.stream()
                .map(nomineeMapper::toNomineeDto)
                .toList();

        return nomineeMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, nomineeDtos);
    }

    /**
     *
     * @param customerIdentity
     * @return
     */
    private Customer findCustomer(UUID customerIdentity) {
        return customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));
    }

    /**
     *
     * @param dto
     */
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

    /**
     *
     * @param customer
     * @param dto
     * @return
     */
    private BigDecimal calculateTotalShare(Customer customer, NomineeDetailsRequestDto dto) {
        BigDecimal share = dto.getPercentageShare() != null ? dto.getPercentageShare() : BigDecimal.valueOf(100);
        return nomineeRepository.findByCustomerIdentity(customer.getIdentity())
                .stream()
                .map(Nominee::getPercentageShare)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(share);
    }

    /**
     *
     * @param customer
     * @param existingNominee
     * @param dto
     * @return
     */
    private BigDecimal calculateTotalShareForUpdate(Customer customer, Nominee existingNominee, NomineeDetailsRequestDto dto) {
        BigDecimal newShare = dto.getPercentageShare() != null ? dto.getPercentageShare() : BigDecimal.valueOf(100);
        BigDecimal totalExistingShares = nomineeRepository.findByCustomerIdentity(customer.getIdentity())
                .stream()
                .filter(n -> !n.getNomineeId().equals(existingNominee.getNomineeId()))
                .map(Nominee::getPercentageShare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalExistingShares.add(newShare);
    }

    /**
     *
     * @param totalShare
     */
    private void validatePercentageShare(BigDecimal totalShare) {
        if (totalShare.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new BusinessException("Total percentage share cannot exceed 100.");
        }
    }

    /**
     *
     * @param dto
     * @param customer
     * @return
     */
    private NomineeAddressDto resolveNomineeAddress(NomineeDetailsRequestDto dto, Customer customer) {
        if (Boolean.TRUE.equals(dto.getIsSameAddress())) {
            var permanentAddressType = addressTypeRepository
                    .findByAddressTypeNameAndIsDelFalse(CommonConstants.ADDRESS_TYPE_PERMANENT)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS, CommonConstants.ADDRESS_TYPE_PERMANENT));

            List<CustomerAddress> customerAddresses = addressRepository
                    .findByCustomerAndAddressTypeIdAndIsActiveTrueAndIsDelFalse(customer, permanentAddressType.getAddressTypeId());

            if (customerAddresses.isEmpty()) {
                throw new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS,
                        "No active permanent address found for customer identity: " + customer.getIdentity());
            }

            return nomineeMapper.toNomineeAddressDtoFromCustomerAddress(customerAddresses.get(0));

        } else {
            if (dto.getAddressTypeId() == null || dto.getDoorNumber() == null || dto.getAddressLine1() == null) {
                throw new BusinessException("Address must be provided when isSameAddress is false");
            }
            return NomineeAddressDto.builder()
                    .addressTypeId(dto.getAddressTypeId())
                    .doorNumber(dto.getDoorNumber())
                    .addressLine1(dto.getAddressLine1())
                    .landmark(dto.getLandmark())
                    .placeName(dto.getPlaceName())
                    .cityId(dto.getCityId())
                    .districtId(dto.getDistrictId())
                    .stateId(dto.getStateId())
                    .countryId(dto.getCountryId())
                    .pincode(dto.getPincode())
                    .postOfficeId(dto.getPostOfficeId())
                    .latitude(dto.getLatitude())
                    .longitude(dto.getLongitude())
                    .digipin(dto.getDigipin())
                    .build();
        }
    }
}
