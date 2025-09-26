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

@Component
@RequiredArgsConstructor
public class CustomerAdditionalInfoMapper {




    public CustomerAsset mapToAsset(CustomerAssetDto assetDto, Customer customer) {
        Objects.requireNonNull(assetDto, "CustomerAssetDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerAsset asset = new CustomerAsset();
        updateAsset(asset, assetDto, customer);
        return asset;
    }

    public CustomerProfileExtra mapToProfileExtra(CustomerProfileExtraDto profileExtraDto, Customer customer) {
        Objects.requireNonNull(profileExtraDto, "CustomerProfileExtraDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerProfileExtra profileExtra = new CustomerProfileExtra();
        updateProfileExtra(profileExtra, profileExtraDto, customer);
        return profileExtra;
    }

//    public CustomerPep mapToPep(CustomerPepDto pepDto, Customer customer) {
//        Objects.requireNonNull(pepDto, "CustomerPepDto must not be null");
//        Objects.requireNonNull(customer, "Customer must not be null");
//
//        CustomerPep pep = new CustomerPep();
//        updatePep(pep, pepDto, customer);
//        return pep;
//    }

    public CustomerReferral mapToReferral(CustomerReferralDto referralDto, Customer customer) {
        Objects.requireNonNull(referralDto, "CustomerReferralDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerReferral referral = new CustomerReferral();
        updateReferral(referral, referralDto, customer);
        return referral;
    }

    public CustomerEmployment mapToEmployment(CustomerEmploymentDto employmentDto, Customer customer) {
        Objects.requireNonNull(employmentDto, "CustomerEmploymentDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerEmployment employment = new CustomerEmployment();
        updateEmployment(employment, employmentDto, customer);
        return employment;
    }



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


    public void createProfileExtra(CustomerProfileExtra profileExtra, CustomerProfileExtraDto profileExtraDto, Customer customer) {
        Objects.requireNonNull(profileExtra, "CustomerProfileExtra must not be null");
        Objects.requireNonNull(profileExtraDto, "CustomerProfileExtraDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        profileExtra.setCustomer(customer);
        profileExtra.setCreatedBy(getCreatedBy());

    }


    public void updateProfileExtra(CustomerProfileExtra profileExtra, CustomerProfileExtraDto profileExtraDto, Customer customer) {
        Objects.requireNonNull(profileExtra, "CustomerProfileExtra must not be null");
        Objects.requireNonNull(profileExtraDto, "CustomerProfileExtraDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        profileExtra.setCustomer(customer);
        profileExtra.setUpdatedBy(getUpdatedBy());
    }



    public void updateReferral(CustomerReferral referral, CustomerReferralDto referralDto, Customer customer) {
        Objects.requireNonNull(referral, "CustomerReferral must not be null");
        Objects.requireNonNull(referralDto, "CustomerReferralDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        referral.setCustomer(customer);
        referral.setCanvasserStaffId(referralDto.getCanvasserStaffId());
        referral.setUpdatedBy(getUpdatedBy());
    }


    public void createReferral(CustomerReferral referral, CustomerReferralDto referralDto, Customer customer) {
        Objects.requireNonNull(referral, "CustomerReferral must not be null");
        Objects.requireNonNull(referralDto, "CustomerReferralDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        referral.setCustomer(customer);
        referral.setCanvasserStaffId(referralDto.getCanvasserStaffId());
        referral.setCreatedBy(getCreatedBy());
    }

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


    public Customer updateCustomerFromAdditionalInfo(Customer customer, AdditionalInfoCustomerDto dto) {
        Objects.requireNonNull(customer, "Customer must not be null");
        if (dto != null) {
            customer.setUpdatedBy(getUpdatedBy());
        }
        return customer;
    }



    public CustomerEmploymentDto mapToEmploymentDto(CustomerEmployment employment) {
        if (employment == null) return null;
        return CustomerEmploymentDto.builder()
                .occupationId(employment.getOccupationId().getIdentity())
                .designationId(employment.getDesignationId().getIdentity())
                .employer(employment.getEmployer())
                .incomeSourceId(employment.getIncomeSourceId() != null ? employment.getIncomeSourceId().getIdentity() : null)
                .monthlySalary(employment.getMonthlySalary())
                .annualIncome(employment.getAnnualIncome())
                .build();
    }

    public CustomerReferralDto mapToReferralDto(CustomerReferral referral) {
        if (referral == null) return null;
        return CustomerReferralDto.builder()
                .referralSourceId(referral.getIdentity())
                .canvassedTypeId(referral.getCanvassedTypeId().getIdentity())
                .canvasserStaffId(referral.getCanvasserStaffId())
                .build();
    }


    public CustomerProfileExtraDto mapToProfileExtraDto(CustomerProfileExtra profileExtra) {
        if (profileExtra == null) return null;
        return CustomerProfileExtraDto.builder()
                .educationLevelId(profileExtra.getEducationLevelId().getIdentity())
                .purposeId(profileExtra.getPurposeId().getIdentity())
                .build();
    }

    public CustomerAssetDto mapToAssetDto(CustomerAsset asset) {
        if (asset == null) return null;
        return CustomerAssetDto.builder()
                .assetTypeId(asset.getAssetTypeId().getIdentity())
                .ownsAsset(asset.getOwnsAsset())
                .homeLoanCompany(asset.getHomeLoanCompany())
                .hasHomeLoan(asset.getHasHomeLoan())
                .build();
    }

    public AdditionalInfoCustomerDto mapToAdditionalInfoCustomerDto(Customer customer) {
        if (customer == null) return null;

        return AdditionalInfoCustomerDto.builder()
                .nationality(customer.getNationality() != null ? customer.getNationality().getIdentity() : null)
                .preferredLanguageId(customer.getPreferredLanguageId() != null ? customer.getPreferredLanguageId().getIdentity() : null)
                .residentialStatusId(customer.getResidentialStatusId() != null ? customer.getResidentialStatusId().getIdentity() : null)
                .customerGroupId(customer.getCustomerGroupId() != null ? customer.getCustomerGroupId().getIdentity() : null)
                .categoryId(customer.getCategoryId() != null ? customer.getCategoryId().getIdentity() : null)
                .riskCategory(customer.getRiskCategory() != null ? customer.getRiskCategory().getIdentity() : null)
                .build();
    }


    public List<AdditionalReferenceValueDto> mapToAdditionalReferenceValueDto(List<CustomerAdditionalReferenceValue> customerAdditionalReferenceValues) {
        if (customerAdditionalReferenceValues == null || customerAdditionalReferenceValues.isEmpty()) {
            return null;
        }
        return customerAdditionalReferenceValues.stream()
                .map(value -> AdditionalReferenceValueDto.builder()
                        .referenceValue(value.getReferenceValue())
                        .referenceIdentity(value.getCustomerAdditionalReferenceName().getIdentity())
                        .build())
                .collect(Collectors.toList());
    }



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

    public  Integer getCreatedBy(){
        return CommonConstants.CREATED_BY;

    }
    public  Integer getUpdatedBy(){
        return CommonConstants.UPDATED_BY;

    }

    public CustomerAdditionalReferenceValue maptoAddtionalRefValue(CustomerAdditionalReferenceValue customerAdditionalReferenceValue,AdditionalReferenceValueDto dto) {
        customerAdditionalReferenceValue.setReferenceValue(dto.getReferenceValue());
        customerAdditionalReferenceValue.setCreatedBy(getCreatedBy());
        customerAdditionalReferenceValue.setIdentity(UUID.randomUUID());

        return customerAdditionalReferenceValue;
    }

    public void createCustomerGroup(CustomerGroup  group , CustomerGroupDto groupDto, Customer customer){
        Objects.requireNonNull(group,"CustomerGroup cannot be null");
        Objects.requireNonNull(groupDto,"GroupDto cannot be null ");
        Objects.requireNonNull(customer,"customer cannot be null");

        group.setCustomer(customer);
        group.setCustomerGroupId(groupDto.getCustomerGroupId());
        group.setTenantId(1);
        group.setGroupName(CommonConstants.DRAFT);

        group.setIsActive(Boolean.TRUE);
        group.setCreatedBy(getCreatedBy());
    }



    public void createRiskCategory(
            CustomerRiskProfile riskProfile
            , CustomerRiskProfileDto riskDto
            , Customer customer ){
        Objects.requireNonNull(riskProfile,"riskProfile cannot be null");
        Objects.requireNonNull(riskDto,"riskDto cannot be null ");
        Objects.requireNonNull(customer,"customer cannot be null");

        riskProfile.setCustomer(customer);
        riskProfile.setAssessmentType(2);
        riskProfile.setAssessmentDate(LocalDate.now());
        riskProfile.setRiskCategory(riskDto.getRiskCategory());
        riskProfile.setRiskScore(new BigDecimal("323"));
        riskProfile.setCreatedBy(getCreatedBy());
    }


}
