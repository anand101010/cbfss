package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerAdditionalInfoMapper;
import com.incede.nbfc.core.monolith.customer.repository.*;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Additional Info Service", description = "Service for managing additional information of customers")
public class CustomerAdditionalInfoService {

    private final CustomerAdditionalInfoMapper mapper;

    private final CustomerRepository customerRepository;
    private final CustomerEmploymentRepository employmentRepository;
    private final CustomerReferralRepository referralRepository;
    private final CustomerProfileExtraRepository profileExtraRepository;
    private final CustomerAssetRepository assetRepository;
    private final CustomerAdditionalReferenceNameRepository referenceNameRepository;
    private final CustomerAdditionalReferenceValueRepository referenceValueRepository;

    private final OccupationRepository occupationRepository;
    private final DesignationsRepository designationsRepository;
    private final SourceOfIncomeTypeRepository sourceOfIncomeTypeRepository;
    private final AssetTypesRepository assetTypesRepository;
    private final EducationLevelsRepository educationLevelsRepository;
    private final PurposeRepository purposeRepository;
    private final ReferralSourceRepository referralSourcesRepository;
    private final CanvassedTypesRepository canvassedTypesRepository;
    private final NationalityRepository nationalityRepository;
    private final ResidentialStatusesRepository residentialStatusesRepository;
    private final LanguagesRepository languagesRepository;
    private final CustomerGroupRepository customerGroupRepository;
    private final CustomerRiskProfileRepository customerRiskProfileRepository;
    private final CustomerCategoryMappingRepository categoryMappingRepository;

    @Operation(summary = "Save Additional Info", description = "Creates or updates additional information for a customer")
    @Transactional
    public CustomerAdditionalInfoResponseDto saveAdditionalInfo(
            @Parameter(description = "UUID of the customer", required = true)
            UUID identity,
            @Parameter(description = "Additional information request DTO", required = true)
            CustomerAdditionalInfoRequestDto dto) {

        Customer customer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new BusinessException(CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        try {
            saveEmployment(customer, dto);
            saveReferral(customer, dto);
            saveProfileExtra(customer, dto);
            saveAsset(customer, dto);
            saveCustomerDetails(customer, dto);
            saveAdditionalReference(customer, dto);

            return buildResponse(customer);
        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation for customer {}: {}", identity, e.getMessage(), e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        }
    }

    @Operation(summary = "Get Additional Info", description = "Fetches all additional information for a customer by UUID")
    @Transactional(readOnly = true)
    public CustomerAdditionalInfoResponseDto getAdditionalInfo(
            @Parameter(description = "UUID of the customer", required = true)
            UUID customerIdentity) {

        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new BusinessException(CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        return buildResponse(customer);
    }

    private void saveEmployment(Customer customer, CustomerAdditionalInfoRequestDto dto) {
        CustomerEmployment employment = employmentRepository.findByCustomer(customer).orElseGet(CustomerEmployment::new);
        mapper.createEmployment(employment, dto.getAdditional().getEmployment(), customer);

        employment.setOccupationId(occupationRepository.findByIdentity(dto.getAdditional().getEmployment().getOccupationId())
                .orElseThrow(() -> new BusinessException("Occupation not found", ErrorCodes.RESOURCE_NOT_FOUND)));
        employment.setDesignationId(designationsRepository.findByIdentity(dto.getAdditional().getEmployment().getDesignationId())
                .orElseThrow(() -> new BusinessException("Designation not found", ErrorCodes.RESOURCE_NOT_FOUND)));
        employment.setIncomeSourceId(sourceOfIncomeTypeRepository.findByIdentity(dto.getAdditional().getEmployment().getIncomeSourceId())
                .orElseThrow(() -> new BusinessException("Income source not found", ErrorCodes.RESOURCE_NOT_FOUND)));

        employmentRepository.save(employment);
    }

    private void saveReferral(Customer customer, CustomerAdditionalInfoRequestDto dto) {
        CustomerReferral referral = referralRepository.findByCustomer(customer).orElseGet(CustomerReferral::new);
        mapper.createReferral(referral, dto.getAdditional().getReferrals(), customer);

        referral.setReferralSources(referralSourcesRepository.findByIdentity(dto.getAdditional().getReferrals().getReferralSourceId())
                .orElseThrow(() -> new BusinessException("Referral source not found", ErrorCodes.RESOURCE_NOT_FOUND)));
        referral.setCanvassedTypeId(canvassedTypesRepository.findByIdentity(dto.getAdditional().getReferrals().getCanvassedTypeId())
                .orElseThrow(() -> new BusinessException("Canvassed type not found", ErrorCodes.RESOURCE_NOT_FOUND)));

        referralRepository.save(referral);
    }

    private void saveProfileExtra(Customer customer, CustomerAdditionalInfoRequestDto dto) {
        CustomerProfileExtra profileExtra = profileExtraRepository.findByCustomer(customer).orElseGet(CustomerProfileExtra::new);
        mapper.createProfileExtra(profileExtra, dto.getAdditional().getProfileExtra(), customer);

        profileExtra.setEducationLevelId(educationLevelsRepository.findByIdentity(dto.getAdditional().getProfileExtra().getEducationLevelId())
                .orElseThrow(() -> new BusinessException("Education level not found", ErrorCodes.RESOURCE_NOT_FOUND)));
        profileExtra.setPurposeId(purposeRepository.findByIdentity(dto.getAdditional().getProfileExtra().getPurposeId())
                .orElseThrow(() -> new BusinessException("Purpose not found", ErrorCodes.RESOURCE_NOT_FOUND)));

        profileExtraRepository.save(profileExtra);
    }

    private void saveAsset(Customer customer, CustomerAdditionalInfoRequestDto dto) {
        CustomerAsset asset = assetRepository.findByCustomer(customer).orElseGet(CustomerAsset::new);
        mapper.createAsset(asset, dto.getAdditional().getCustomerAsset(), customer);

        asset.setAssetTypeId(assetTypesRepository.findByIdentity(dto.getAdditional().getCustomerAsset().getAssetTypeId())
                .orElseThrow(() -> new BusinessException("Asset type not found", ErrorCodes.RESOURCE_NOT_FOUND)));

        assetRepository.save(asset);
    }

    private void saveCustomerDetails(Customer customer, CustomerAdditionalInfoRequestDto dto) {
        Customer updatedCustomer = mapper.updateCustomerFromAdditionalInfo(customer, dto.getAdditional().getCustomer());

        updatedCustomer.setNationality(nationalityRepository.findByIdentity(dto.getAdditional().getCustomer().getNationality())
                .orElseThrow(() -> new BusinessException("Nationality not found", ErrorCodes.RESOURCE_NOT_FOUND)));
        updatedCustomer.setPreferredLanguageId(languagesRepository.findByIdentity(dto.getAdditional().getCustomer().getPreferredLanguageId())
                .orElseThrow(() -> new BusinessException("Preferred language not found", ErrorCodes.RESOURCE_NOT_FOUND)));
        updatedCustomer.setResidentialStatusId(residentialStatusesRepository.findByIdentity(dto.getAdditional().getCustomer().getResidentialStatusId())
                .orElseThrow(() -> new BusinessException("Residential status not found", ErrorCodes.RESOURCE_NOT_FOUND)));
        updatedCustomer.setCustomerGroupId(customerGroupRepository.findByIdentity(dto.getAdditional().getCustomer().getCustomerGroupId()).orElse(null));
        updatedCustomer.setRiskCategory(customerRiskProfileRepository.findByIdentity(dto.getAdditional().getCustomer().getRiskCategory()).orElse(null));
        updatedCustomer.setCategoryId(categoryMappingRepository.findByIdentity(dto.getAdditional().getCustomer().getCategoryId()).orElse(null));

        customerRepository.save(updatedCustomer);
    }

    private void saveAdditionalReference(Customer customer, CustomerAdditionalInfoRequestDto dto) {
        CustomerAdditionalReferenceValue referenceValue = referenceValueRepository.findByCustomer(customer)
                .orElseGet(CustomerAdditionalReferenceValue::new);
        mapper.maptoAddtionalRefValue(referenceValue, dto.getAdditional().getAdditionalReferenceValueDto());

        referenceValue.setCustomerAdditionalReferenceName(referenceNameRepository.findByIdentity(
                        dto.getAdditional().getAdditionalReferenceValueDto().getReferenceIdentity())
                .orElseThrow(() -> new BusinessException("Customer additional reference name not found", ErrorCodes.RESOURCE_NOT_FOUND)));

        referenceValue.setCustomer(customer);
        referenceValueRepository.save(referenceValue);
    }

    private CustomerAdditionalInfoResponseDto buildResponse(Customer customer) {
        CustomerEmployment employment = employmentRepository.findByCustomer(customer).orElse(null);
        CustomerReferral referral = referralRepository.findByCustomer(customer).orElse(null);
        CustomerProfileExtra profileExtra = profileExtraRepository.findByCustomer(customer).orElse(null);
        CustomerAsset asset = assetRepository.findByCustomer(customer).orElse(null);
        CustomerAdditionalReferenceValue referenceValue = referenceValueRepository.findByCustomer(customer).orElse(null);

        return mapper.buildResponseDto(customer, employment, referral, profileExtra, asset, referenceValue);
    }


}
