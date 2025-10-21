package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Mapper for converting between customer additional information entities and DTOs.
 */
@Component
@RequiredArgsConstructor
public class CustomerAdditionalInfoMapper {

    /**
     * Converts a CustomerAssetDto to a CustomerAsset entity.
     *
     * @param assetDto Customer asset DTO
     * @param customer Customer entity
     * @return Mapped CustomerAsset entity
     */
    public CustomerAsset mapToAsset(CustomerAssetDto assetDto, Customer customer) {
        Objects.requireNonNull(assetDto, "CustomerAssetDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerAsset asset = new CustomerAsset();
        updateAsset(asset, assetDto, customer);
        return asset;
    }

    /**
     * Converts a CustomerProfileExtraDto to a CustomerProfileExtra entity.
     *
     * @param profileExtraDto Customer profile extra DTO
     * @param customer Customer entity
     * @return Mapped CustomerProfileExtra entity
     */
    public CustomerProfileExtra mapToProfileExtra(CustomerProfileExtraDto profileExtraDto, Customer customer) {
        Objects.requireNonNull(profileExtraDto, "CustomerProfileExtraDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerProfileExtra profileExtra = new CustomerProfileExtra();
        updateProfileExtra(profileExtra, profileExtraDto, customer);
        return profileExtra;
    }

    /**
     * Converts a CustomerReferralDto to a CustomerReferral entity.
     *
     * @param referralDto Customer referral DTO
     * @param customer Customer entity
     * @return Mapped CustomerReferral entity
     */
    public CustomerReferral mapToReferral(CustomerReferralDto referralDto, Customer customer) {
        Objects.requireNonNull(referralDto, "CustomerReferralDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerReferral referral = new CustomerReferral();
        updateReferral(referral, referralDto, customer);
        return referral;
    }

    /**
     * Converts a CustomerEmploymentDto to a CustomerEmployment entity.
     *
     * @param employmentDto Customer employment DTO
     * @param customer Customer entity
     * @return Mapped CustomerEmployment entity
     */
    public CustomerEmployment mapToEmployment(CustomerEmploymentDto employmentDto, Customer customer) {
        Objects.requireNonNull(employmentDto, "CustomerEmploymentDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerEmployment employment = new CustomerEmployment();
        updateEmployment(employment, employmentDto, customer);
        return employment;
    }

    /**
     * Updates an existing CustomerAsset entity with values from a DTO.
     *
     * @param asset CustomerAsset entity to update
     * @param assetDto CustomerAssetDto with updated values
     * @param customer Customer entity
     */
    public void updateAsset(CustomerAsset asset, CustomerAssetDto assetDto, Customer customer) {
        Objects.requireNonNull(asset, "CustomerAsset must not be null");
        Objects.requireNonNull(assetDto, "CustomerAssetDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        asset.setCustomer(customer);
        asset.setHasHomeLoan(assetDto.getHasHomeLoan() != null ? assetDto.getHasHomeLoan() : false);

        if (Boolean.TRUE.equals(assetDto.getHasHomeLoan())) {
            asset.setHomeLoanCompany(assetDto.getHomeLoanCompany());
        } else {
            asset.setHomeLoanCompany(null);
        }

        asset.setUpdatedBy(getUpdatedBy());
    }

    /**
     * Creates a new CustomerAsset entity from a DTO.
     *
     * @param asset CustomerAsset entity to populate
     * @param assetDto CustomerAssetDto with asset details
     * @param customer Customer entity
     */
    public void createAsset(CustomerAsset asset, CustomerAssetDto assetDto, Customer customer) {
        Objects.requireNonNull(asset, "CustomerAsset must not be null");
        Objects.requireNonNull(assetDto, "CustomerAssetDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        asset.setCustomer(customer);
        asset.setOwnsAsset(assetDto.getOwnsAsset() != null ? assetDto.getOwnsAsset() : false);
        asset.setHasHomeLoan(assetDto.getHasHomeLoan() != null ? assetDto.getHasHomeLoan() : false);

        if (Boolean.TRUE.equals(assetDto.getHasHomeLoan())) {
            asset.setHomeLoanCompany(assetDto.getHomeLoanCompany());
        } else {
            asset.setHomeLoanCompany(null);
        }
        asset.setCreatedBy(getCreatedBy());
    }

    /**
     * Creates a new CustomerProfileExtra entity from a DTO.
     *
     * @param profileExtra CustomerProfileExtra entity to populate
     * @param profileExtraDto CustomerProfileExtraDto with profile details
     * @param customer Customer entity
     */
    public void createProfileExtra(CustomerProfileExtra profileExtra, CustomerProfileExtraDto profileExtraDto, Customer customer) {
        Objects.requireNonNull(profileExtra, "CustomerProfileExtra must not be null");
        Objects.requireNonNull(profileExtraDto, "CustomerProfileExtraDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        profileExtra.setCustomer(customer);
        profileExtra.setCreatedBy(getCreatedBy());
    }

    /**
     * Updates an existing CustomerProfileExtra entity with values from a DTO.
     *
     * @param profileExtra CustomerProfileExtra entity to update
     * @param profileExtraDto CustomerProfileExtraDto with updated values
     * @param customer Customer entity
     */
    public void updateProfileExtra(CustomerProfileExtra profileExtra, CustomerProfileExtraDto profileExtraDto, Customer customer) {
        Objects.requireNonNull(profileExtra, "CustomerProfileExtra must not be null");
        Objects.requireNonNull(profileExtraDto, "CustomerProfileExtraDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        profileExtra.setCustomer(customer);
        profileExtra.setUpdatedBy(getUpdatedBy());
    }

    /**
     * Updates an existing CustomerReferral entity with values from a DTO.
     *
     * @param referral CustomerReferral entity to update
     * @param referralDto CustomerReferralDto with updated values
     * @param customer Customer entity
     */
    public void updateReferral(CustomerReferral referral, CustomerReferralDto referralDto, Customer customer) {
        Objects.requireNonNull(referral, "CustomerReferral must not be null");
        Objects.requireNonNull(referralDto, "CustomerReferralDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        referral.setCustomer(customer);
        referral.setCanvasserStaffId(referralDto.getCanvasserStaffId());
        referral.setUpdatedBy(getUpdatedBy());
    }

    /**
     * Creates a new CustomerReferral entity from a DTO.
     *
     * @param referral CustomerReferral entity to populate
     * @param referralDto CustomerReferralDto with referral details
     * @param customer Customer entity
     */
    public void createReferral(CustomerReferral referral, CustomerReferralDto referralDto, Customer customer) {
        Objects.requireNonNull(referral, "CustomerReferral must not be null");
        Objects.requireNonNull(referralDto, "CustomerReferralDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        referral.setCustomer(customer);
        referral.setCanvasserStaffId(referralDto.getCanvasserStaffId());
        referral.setCreatedBy(getCreatedBy());
    }

    /**
     * Updates an existing CustomerEmployment entity with values from a DTO.
     *
     * @param employment CustomerEmployment entity to update
     * @param employmentDto CustomerEmploymentDto with updated values
     * @param customer Customer entity
     */
    public void updateEmployment(CustomerEmployment employment, CustomerEmploymentDto employmentDto, Customer customer) {
        Objects.requireNonNull(employment, "CustomerEmployment must not be null");
        Objects.requireNonNull(employmentDto, "CustomerEmploymentDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        employment.setCustomer(customer);
        employment.setEmployer(employmentDto.getEmployer());
        employment.setMonthlySalary(employmentDto.getMonthlySalary());
        employment.setAnnualIncome(employmentDto.getAnnualIncome());
        employment.setUpdatedBy(getUpdatedBy());
    }

    /**
     * Creates a new CustomerEmployment entity from a DTO.
     *
     * @param employment CustomerEmployment entity to populate
     * @param employmentDto CustomerEmploymentDto with employment details
     * @param customer Customer entity
     */
    public void createEmployment(CustomerEmployment employment, CustomerEmploymentDto employmentDto, Customer customer) {
        Objects.requireNonNull(employment, "CustomerEmployment must not be null");
        Objects.requireNonNull(employmentDto, "CustomerEmploymentDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        employment.setCustomer(customer);
        employment.setEmployer(employmentDto.getEmployer());
        employment.setMonthlySalary(employmentDto.getMonthlySalary());
        employment.setAnnualIncome(employmentDto.getAnnualIncome());
        employment.setCreatedBy(getCreatedBy());
    }

    /**
     * Updates a Customer entity with additional information from a DTO.
     *
     * @param customer Customer entity to update
     * @param dto AdditionalInfoCustomerDto with additional details
     * @return Updated Customer entity
     */
    public Customer updateCustomerFromAdditionalInfo(Customer customer, AdditionalInfoCustomerDto dto) {
        Objects.requireNonNull(customer, "Customer must not be null");
        if (dto != null) {
            customer.setUpdatedBy(getUpdatedBy());
        }
        return customer;
    }

    /**
     * Converts a CustomerEmployment entity to a CustomerEmploymentDto.
     *
     * @param employment CustomerEmployment entity
     * @return Mapped CustomerEmploymentDto
     */
    public CustomerEmploymentDto mapToEmploymentDto(CustomerEmployment employment) {
        if (employment == null) return null;
        return CustomerEmploymentDto.builder()
                .occupationId(employment.getOccupationId() != null ?
                        employment.getOccupationId().getIdentity() : null)
                .designationId(employment.getDesignationId() != null ?
                        employment.getDesignationId().getIdentity() : null)
                .employer(employment.getEmployer())
                .incomeSourceId(employment.getIncomeSourceId() != null ?
                        employment.getIncomeSourceId().getIdentity() : null)
                .monthlySalary(employment.getMonthlySalary())
                .annualIncome(employment.getAnnualIncome())
                .build();
    }

    /**
     * Converts a CustomerReferral entity to a CustomerReferralDto.
     *
     * @param referral CustomerReferral entity
     * @return Mapped CustomerReferralDto
     */
    public CustomerReferralDto mapToReferralDto(CustomerReferral referral) {
        if (referral == null) return null;
        return CustomerReferralDto.builder()
                .referralSourceId(referral.getIdentity())
                .canvassedTypeId(referral.getCanvassedTypeId() != null ?
                        referral.getCanvassedTypeId().getIdentity() : null)
                .canvasserStaffId(referral.getCanvasserStaffId())
                .build();
    }

    /**
     * Converts a CustomerProfileExtra entity to a CustomerProfileExtraDto.
     *
     * @param profileExtra CustomerProfileExtra entity
     * @return Mapped CustomerProfileExtraDto
     */
    public CustomerProfileExtraDto mapToProfileExtraDto(CustomerProfileExtra profileExtra) {
        if (profileExtra == null) return null;
        return CustomerProfileExtraDto.builder()
                .educationLevelId(profileExtra.getEducationLevelId() != null ?
                        profileExtra.getEducationLevelId().getIdentity() : null)
                .purposeId(profileExtra.getPurposeId() != null ?
                        profileExtra.getPurposeId().getIdentity() : null)
                .build();
    }

    /**
     * Converts a CustomerAsset entity to a CustomerAssetDto.
     *
     * @param asset CustomerAsset entity
     * @return Mapped CustomerAssetDto
     */
    public CustomerAssetDto mapToAssetDto(CustomerAsset asset) {
        if (asset == null) return null;
        return CustomerAssetDto.builder()
                .assetTypeId(asset.getAssetTypeId() != null ?
                        asset.getAssetTypeId().getIdentity() : null)
                .ownsAsset(asset.getOwnsAsset())
                .homeLoanCompany(asset.getHomeLoanCompany())
                .hasHomeLoan(asset.getHasHomeLoan())
                .build();
    }

    /**
     * Converts a Customer entity to an AdditionalInfoCustomerDto.
     *
     * @param customer Customer entity
     * @return Mapped AdditionalInfoCustomerDto
     */
    public AdditionalInfoCustomerDto mapToAdditionalInfoCustomerDto(Customer customer) {
        if (customer == null) return null;
        return AdditionalInfoCustomerDto.builder()
                .nationality(customer.getNationality() != null ?
                        customer.getNationality().getIdentity() : null)
                .preferredLanguageId(customer.getPreferredLanguageId() != null ?
                        customer.getPreferredLanguageId().getIdentity() : null)
                .residentialStatusId(customer.getResidentialStatusId() != null ?
                        customer.getResidentialStatusId().getIdentity() : null)
                .customerGroupId(customer.getCustomerGroupId() != null ?
                        customer.getCustomerGroupId().getIdentity() : null)
                .categoryId(customer.getCategoryId() != null ?
                        customer.getCategoryId().getIdentity() : null)
                .riskCategory(customer.getRiskCategory() != null ?
                        customer.getRiskCategory().getIdentity() : null)
                .build();
    }

    /**
     * Converts a list of CustomerAdditionalReferenceValue entities to AdditionalReferenceValueDto DTOs.
     *
     * @param customerAdditionalReferenceValues List of additional reference value entities
     * @return List of AdditionalReferenceValueDto DTOs
     */
    public List<AdditionalReferenceValueDto> mapToAdditionalReferenceValueDto(List<CustomerAdditionalReferenceValue> customerAdditionalReferenceValues) {
        if (customerAdditionalReferenceValues == null || customerAdditionalReferenceValues.isEmpty()) {
            return new ArrayList<>();
        }
        return customerAdditionalReferenceValues.stream()
                .map(value -> AdditionalReferenceValueDto.builder()
                        .referenceValue(value.getReferenceValue())
                        .referenceIdentity(value.getCustomerAdditionalReferenceName() != null ?
                                value.getCustomerAdditionalReferenceName().getIdentity() : null)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Builds a response DTO from customer and additional information entities.
     *
     * @param customer Customer entity
     * @param employment CustomerEmployment entity
     * @param referral CustomerReferral entity
     * @param profileExtra CustomerProfileExtra entity
     * @param assets CustomerAsset entity
     * @param additionalReferenceValues List of additional reference value entities
     * @return CustomerAdditionalInfoResponseDto with additional information
     */
    public CustomerAdditionalInfoResponseDto buildResponseDto(
            Customer customer,
            CustomerEmployment employment,
            CustomerReferral referral,
            CustomerProfileExtra profileExtra,
            CustomerAsset assets,
            List<CustomerAdditionalReferenceValue> additionalReferenceValues) {

        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerAdditionalInfoResponseDto.AdditionalInfoDto additional =
                CustomerAdditionalInfoResponseDto.AdditionalInfoDto.builder()
                        .employment(mapToEmploymentDto(employment))
                        .referrals(mapToReferralDto(referral))
                        .profileExtra(mapToProfileExtraDto(profileExtra))
                        .assets(mapToAssetDto(assets))
                        .additionalInfoCustomerDto(mapToAdditionalInfoCustomerDto(customer))
                        .additionalReferenceValueDto(mapToAdditionalReferenceValueDto(additionalReferenceValues))
                        .build();

        return CustomerAdditionalInfoResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status(customer.getOnboardingStatus())
                .additional(additional)
                .build();
    }

    /**
     * Retrieves the created-by user ID.
     *
     * @return Created-by user ID from CommonConstants
     */
    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    /**
     * Retrieves the updated-by user ID.
     *
     * @return Updated-by user ID from CommonConstants
     */
    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }

    /**
     * Maps an AdditionalReferenceValueDto to a CustomerAdditionalReferenceValue entity.
     *
     * @param customerAdditionalReferenceValue CustomerAdditionalReferenceValue entity to populate
     * @param dto AdditionalReferenceValueDto with reference details
     * @return Mapped CustomerAdditionalReferenceValue entity
     */
    public CustomerAdditionalReferenceValue maptoAddtionalRefValue(CustomerAdditionalReferenceValue customerAdditionalReferenceValue, AdditionalReferenceValueDto dto) {
        customerAdditionalReferenceValue.setReferenceValue(dto.getReferenceValue());
        customerAdditionalReferenceValue.setCreatedBy(getCreatedBy());
        customerAdditionalReferenceValue.setIdentity(UUID.randomUUID());

        return customerAdditionalReferenceValue;
    }

    /**
     * Creates a new CustomerGroup entity from a DTO.
     *
     * @param group CustomerGroup entity to populate
     * @param groupDto CustomerGroupDto with group details
     * @param customer Customer entity
     */
    public void createCustomerGroup(CustomerGroup group, CustomerGroupDto groupDto, Customer customer) {
        Objects.requireNonNull(group, "CustomerGroup cannot be null");
        Objects.requireNonNull(groupDto, "GroupDto cannot be null");
        Objects.requireNonNull(customer, "Customer cannot be null");

        group.setCustomer(customer);
        group.setCustomerGroupId(groupDto.getCustomerGroupId());
        group.setTenantId(1);
        group.setGroupName(CommonConstants.DRAFT);

        group.setIsActive(Boolean.TRUE);
        group.setCreatedBy(getCreatedBy());
    }

    /**
     * Creates a new CustomerRiskProfile entity from a DTO.
     *
     * @param riskProfile CustomerRiskProfile entity to populate
     * @param riskDto CustomerRiskProfileDto with risk details
     * @param customer Customer entity
     */
    public void createRiskCategory(CustomerRiskProfile riskProfile, CustomerRiskProfileDto riskDto, Customer customer) {
        Objects.requireNonNull(riskProfile, "CustomerRiskProfile cannot be null");
        Objects.requireNonNull(riskDto, "CustomerRiskProfileDto cannot be null");
        Objects.requireNonNull(customer, "Customer cannot be null");

        riskProfile.setCustomer(customer);
        riskProfile.setAssessmentType(2);
        riskProfile.setAssessmentDate(LocalDate.now());
        riskProfile.setRiskCategory(riskDto.getRiskCategory());
        riskProfile.setRiskScore(new BigDecimal("323"));
        riskProfile.setCreatedBy(getCreatedBy());
    }
}