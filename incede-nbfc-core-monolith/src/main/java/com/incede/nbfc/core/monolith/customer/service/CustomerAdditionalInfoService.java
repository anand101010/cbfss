package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.AdditionalReferenceValueDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerAdditionalInfoMapper;
import com.incede.nbfc.core.monolith.customer.repository.*;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service class for managing additional customer information such as employment,
 * referrals, profile extras, assets, and reference values.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Additional Info", description = "Service for managing customer additional information")
public class CustomerAdditionalInfoService {

    private final CustomerAdditionalInfoMapper customerAdditionalInfoMapper;

    private final CustomerRepository customerRepository;
    private final CustomerEmploymentRepository employmentRepository;
    private final ReferralSourceRepository referralRepository;
    private final CustomerReferralRepository customerReferralRepository;
    private final CustomerProfileExtraRepository profileExtraRepository;
    private final CustomerAssetRepository assetRepository;
    private final OccupationRepository occupationRepository;
    private final DesignationsRepository designationsRepository;
    private final SourceOfIncomeTypeRepository sourceOfIncomeTypeRepository;
    private final AssetTypesRepository assetTypesRepository;
    private final EducationLevelsRepository educationLevelsRepository;
    private final PurposeRepository purposeRepository;
    private final CanvassedTypesRepository canvassedTypesRepository;
    private final NationalityRepository nationalityRepository;
    private final ResidentialStatusesRepository residentialStatusesRepository;
    private final LanguagesRepository languagesRepository;
    private final CustomerAdditionalReferenceNameRepository customerAdditionalReferenceNameRepository;
    private final CustomerAdditionalReferenceValueRepository customerAdditionalReferenceValueRepository;
    private final CustomerGroupMasterRepository customerGroupMasterRepository;
    private final RiskCategoryRepository riskCategoryRepository;
    private final CustomerCategoryRepository customerCategoryRepository;

    /**
     * Save additional information for a given customer.
     *
     * @param identity customer UUID
     * @param dto      DTO containing the additional info
     * @return saved CustomerAdditionalInfoResponseDto
     */
    @Transactional
    public CustomerAdditionalInfoResponseDto saveAdditionalInfo(
            @Parameter(description = "UUID of the customer", required = true)
            UUID identity,
            CustomerAdditionalInfoRequestDto dto) {

        log.info("Saving additional info for customer with identity: {}", identity);

        Customer customer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new BusinessException(CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        try {

            CustomerEmployment employment = employmentRepository.findByCustomer(customer).orElseGet(CustomerEmployment::new);
            customerAdditionalInfoMapper.createEmployment(employment, dto.getAdditional().getEmployment(), customer);
            employment.setOccupationId(occupationRepository.findByIdentity(dto.getAdditional().getEmployment().getOccupationId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.OCCUPATION_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            employment.setDesignationId(designationsRepository.findByIdentity(dto.getAdditional().getEmployment().getDesignationId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.DESIGNATION_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            employment.setIncomeSourceId(sourceOfIncomeTypeRepository.findByIdentity(dto.getAdditional().getEmployment().getIncomeSourceId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.INCOME_SOURCE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            employmentRepository.save(employment);


            CustomerReferral referral = customerReferralRepository.findByCustomer(customer).orElseGet(CustomerReferral::new);
            customerAdditionalInfoMapper.createReferral(referral, dto.getAdditional().getReferrals(), customer);
            referral.setReferralSources(referralRepository.findByIdentity(dto.getAdditional().getReferrals().getReferralSourceId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.REFERRAL_SOURCE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            referral.setCanvassedTypeId(canvassedTypesRepository.findByIdentity(dto.getAdditional().getReferrals().getCanvassedTypeId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.CANVASSED_TYPE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            customerReferralRepository.save(referral);


            CustomerProfileExtra profileExtra = profileExtraRepository.findByCustomer(customer).orElseGet(CustomerProfileExtra::new);
            customerAdditionalInfoMapper.createProfileExtra(profileExtra, dto.getAdditional().getProfileExtra(), customer);
            profileExtra.setEducationLevelId(educationLevelsRepository.findByIdentity(dto.getAdditional().getProfileExtra().getEducationLevelId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.EDUCATION_LEVEL_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            profileExtra.setPurposeId(purposeRepository.findByIdentity(dto.getAdditional().getProfileExtra().getPurposeId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.PURPOSE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            profileExtraRepository.save(profileExtra);


            CustomerAsset asset = assetRepository.findByCustomer(customer).orElseGet(CustomerAsset::new);
            customerAdditionalInfoMapper.createAsset(asset, dto.getAdditional().getCustomerAsset(), customer);
            if(dto.getAdditional().getCustomerAsset().getOwnsAsset().equals(Boolean.TRUE)) {
                asset.setAssetTypeId(assetTypesRepository.findByIdentity(dto.getAdditional().getCustomerAsset().getAssetTypeId())
                        .orElseThrow(() -> new BusinessException(CommonConstants.ASSET_TYPE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            }
            assetRepository.save(asset);



            Customer updatedCustomer = customerAdditionalInfoMapper.updateCustomerFromAdditionalInfo(customer, dto.getAdditional().getCustomer());
            updatedCustomer.setNationality(nationalityRepository.findByIdentity(dto.getAdditional().getCustomer().getNationality())
                    .orElseThrow(() -> new BusinessException(CommonConstants.NATIONALITY_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            updatedCustomer.setPreferredLanguageId(languagesRepository.findByIdentity(dto.getAdditional().getCustomer().getPreferredLanguageId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.PREFERRED_LANGUAGE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            updatedCustomer.setResidentialStatusId(residentialStatusesRepository.findByIdentity(dto.getAdditional().getCustomer().getResidentialStatusId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.RESIDENTIAL_STATUS_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            updatedCustomer.setCustomerGroupId(customerGroupMasterRepository.findByIdentity(dto.getAdditional().getCustomer().getCustomerGroupId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_GROUP_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            updatedCustomer.setRiskCategory(riskCategoryRepository.findByIdentity(dto.getAdditional().getCustomer().getRiskCategory())
                    .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_RISK_CATEGORY_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            updatedCustomer.setCategoryId(customerCategoryRepository.findByIdentity(dto.getAdditional().getCustomer().getCategoryId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.CATEGORY_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND)));
            customerRepository.save(updatedCustomer);


            List<CustomerAdditionalReferenceValue> customerAdditionalReferenceValues = new ArrayList<>();
            List<AdditionalReferenceValueDto> referenceValueDtos = dto.getAdditional().getAdditionalReferenceValueDto();
            if (referenceValueDtos != null) {
                for (AdditionalReferenceValueDto referenceValueDto : referenceValueDtos) {
                    CustomerAdditionalReferenceValue customerAdditionalReferenceValue = customerAdditionalReferenceValueRepository
                            .findByCustomerAndCustomerAdditionalReferenceName(
                                    customer,
                                    customerAdditionalReferenceNameRepository.findByIdentity(referenceValueDto.getReferenceIdentity())
                                            .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_ADDITIONAL_REF_NAME_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND))
                            )
                            .orElseGet(CustomerAdditionalReferenceValue::new);

                    customerAdditionalInfoMapper.maptoAddtionalRefValue(customerAdditionalReferenceValue, referenceValueDto);
                    customerAdditionalReferenceValue.setCustomerAdditionalReferenceName(
                            customerAdditionalReferenceNameRepository.findByIdentity(referenceValueDto.getReferenceIdentity())
                                    .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_ADDITIONAL_REF_NAME_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND))
                    );
                    customerAdditionalReferenceValue.setCustomer(customer);
                    customerAdditionalReferenceValueRepository.save(customerAdditionalReferenceValue);
                    customerAdditionalReferenceValues.add(customerAdditionalReferenceValue);
                }
            }

            log.info("Successfully saved additional info for customer: {}", identity);
            return customerAdditionalInfoMapper.buildResponseDto(customer, employment, referral, profileExtra, asset, customerAdditionalReferenceValues);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving additional info for customer: {} DTO: {}", identity, dto, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IllegalArgumentException e) {
            log.warn("Validation failed while saving additional info. DTO: {} - {}", dto, e.getMessage());
            throw new BusinessException(e.getMessage(), ErrorCodes.VALIDATION_FAILED, e);
        }
    }

    /**
     * Fetch all additional information for a given customer.
     *
     * @param customerIdentity customer UUID
     * @return CustomerAdditionalInfoResponseDto
     */
    @Transactional(readOnly = true)
    public CustomerAdditionalInfoResponseDto getAdditionalInfo(
            @Parameter(description = "UUID of the customer", required = true)
            UUID customerIdentity) {

        log.info("Fetching additional info for customer with identity: {}", customerIdentity);

        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new BusinessException(CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        CustomerEmployment employment = employmentRepository.findByCustomer(customer).orElse(null);
        CustomerReferral referral = customerReferralRepository.findByCustomer(customer).orElse(null);
        CustomerProfileExtra profileExtra = profileExtraRepository.findByCustomer(customer).orElse(null);
        CustomerAsset assets = assetRepository.findByCustomer(customer).orElse(null);
        List<CustomerAdditionalReferenceValue> customerAdditionalReferenceValues = customerAdditionalReferenceValueRepository.findAllByCustomer(customer);

        log.info("Successfully fetched additional info for customer: {}", customerIdentity);
        return customerAdditionalInfoMapper.buildResponseDto(customer, employment, referral, profileExtra, assets, customerAdditionalReferenceValues);
    }
}
