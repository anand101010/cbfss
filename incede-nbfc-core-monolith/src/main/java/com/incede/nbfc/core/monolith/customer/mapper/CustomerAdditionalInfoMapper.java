package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
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

    public CustomerPep mapToPep(CustomerPepDto pepDto, Customer customer) {
        Objects.requireNonNull(pepDto, "CustomerPepDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerPep pep = new CustomerPep();
        updatePep(pep, pepDto, customer);
        return pep;
    }

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
        asset.setAssetId(assetDto.getAssetId());
        asset.setAssetTypeId(assetDto.getAssetTypeId());
        asset.setDescription(assetDto.getDescription());
        asset.setApproxValue(assetDto.getApproxValue());
        asset.setOwnsAsset(assetDto.getOwnsAsset() != null ? assetDto.getOwnsAsset() : false);
        asset.setHomeLoanCompany(assetDto.getHomeLoanCompany() != null ? assetDto.getHomeLoanCompany() : "");
        asset.setHasHomeLoan(assetDto.getHasHomeLoan() != null ? assetDto.getHasHomeLoan() : false);
        asset.setCreatedBy(assetDto.getCreatedBy());
        asset.setUpdatedBy(assetDto.getUpdatedBy());
    }

    public void updateProfileExtra(CustomerProfileExtra profileExtra, CustomerProfileExtraDto profileExtraDto, Customer customer) {
        Objects.requireNonNull(profileExtra, "CustomerProfileExtra must not be null");
        Objects.requireNonNull(profileExtraDto, "CustomerProfileExtraDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        profileExtra.setCustomer(customer);
        profileExtra.setEducationLevelId(profileExtraDto.getEducationLevelId());
        profileExtra.setPurposeId(profileExtraDto.getPurposeId());
        profileExtra.setCreatedBy(profileExtraDto.getCreatedBy());
        profileExtra.setUpdatedBy(profileExtraDto.getUpdatedBy());
    }

    public void updatePep(CustomerPep pep, CustomerPepDto pepDto, Customer customer) {
        Objects.requireNonNull(pep, "CustomerPep must not be null");
        Objects.requireNonNull(pepDto, "CustomerPepDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        pep.setCustomer(customer);
        pep.setStatus(pepDto.getStatus());
        pep.setCategoryId(pepDto.getCategoryId());
        pep.setRelationshipId(pepDto.getRelationshipId());
        pep.setVerificationSourceId(pepDto.getVerificationSourceId());
        pep.setCreatedBy(pepDto.getCreatedBy());
        pep.setUpdatedBy(pepDto.getUpdatedBy());
    }

    public void updateReferral(CustomerReferral referral, CustomerReferralDto referralDto, Customer customer) {
        Objects.requireNonNull(referral, "CustomerReferral must not be null");
        Objects.requireNonNull(referralDto, "CustomerReferralDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        referral.setCustomer(customer);
        referral.setReferralSourceId(referralDto.getReferralSourceId());
        referral.setCanvassedTypeId(referralDto.getCanvassedTypeId());
        referral.setCanvasserStaffId(referralDto.getCanvasserStaffId());
        referral.setCreatedBy(referralDto.getCreatedBy());
        referral.setUpdatedBy(referralDto.getUpdatedBy());
    }

    public void updateEmployment(CustomerEmployment employment, CustomerEmploymentDto employmentDto, Customer customer) {
        Objects.requireNonNull(employment, "CustomerEmployment must not be null");
        Objects.requireNonNull(employmentDto, "CustomerEmploymentDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        employment.setCustomer(customer);
        employment.setOccupationId(employmentDto.getOccupationId());
        employment.setDesignationId(employmentDto.getDesignationId());
        employment.setEmployer(employmentDto.getEmployer());
        employment.setIncomeSourceId(employmentDto.getIncomeSourceId());
        employment.setMonthlySalary(employmentDto.getMonthlySalary());
        employment.setAnnualIncome(employmentDto.getAnnualIncome());
        employment.setCreatedBy(employmentDto.getCreatedBy());
        employment.setUpdatedBy(employmentDto.getUpdatedBy());
    }

    public Customer updateCustomerFromAdditionalInfo(Customer customer, AdditionalInfoCustomerDto dto) {
        Objects.requireNonNull(customer, "Customer must not be null");
        if (dto != null) {
            customer.setNationality(dto.getNationality());
            customer.setPreferredLanguageId(dto.getPreferredLanguageId());
            customer.setResidentialStatusId(dto.getResidentialStatusId());
        }
        return customer;
    }



    public CustomerEmploymentDto mapToEmploymentDto(CustomerEmployment employment) {
        if (employment == null) return null;
        return CustomerEmploymentDto.builder()
                .occupationId(employment.getOccupationId())
                .designationId(employment.getDesignationId())
                .employer(employment.getEmployer())
                .incomeSourceId(employment.getIncomeSourceId())
                .monthlySalary(employment.getMonthlySalary())
                .annualIncome(employment.getAnnualIncome())
                .createdBy(employment.getCreatedBy())
                .updatedBy(employment.getUpdatedBy())
                .build();
    }

    public CustomerReferralDto mapToReferralDto(CustomerReferral referral) {
        if (referral == null) return null;
        return CustomerReferralDto.builder()
                .referralSourceId(referral.getReferralSourceId())
                .canvassedTypeId(referral.getCanvassedTypeId())
                .canvasserStaffId(referral.getCanvasserStaffId())
                .createdBy(referral.getCreatedBy())
                .updatedBy(referral.getUpdatedBy())
                .build();
    }

    public CustomerPepDto mapToPepDto(CustomerPep pep) {
        if (pep == null) return null;
        return CustomerPepDto.builder()
                .status(pep.getStatus())
                .categoryId(pep.getCategoryId())
                .relationshipId(pep.getRelationshipId())
                .verificationSourceId(pep.getVerificationSourceId())
                .createdBy(pep.getCreatedBy())
                .updatedBy(pep.getUpdatedBy())
                .build();
    }

    public CustomerProfileExtraDto mapToProfileExtraDto(CustomerProfileExtra profileExtra) {
        if (profileExtra == null) return null;
        return CustomerProfileExtraDto.builder()
                .educationLevelId(profileExtra.getEducationLevelId())
                .purposeId(profileExtra.getPurposeId())
                .createdBy(profileExtra.getCreatedBy())
                .updatedBy(profileExtra.getUpdatedBy())
                .build();
    }

    public CustomerAssetDto mapToAssetDto(CustomerAsset asset) {
        if (asset == null) return null;
        return CustomerAssetDto.builder()
                .assetId(asset.getAssetId())
                .assetTypeId(asset.getAssetTypeId())
                .description(asset.getDescription())
                .approxValue(asset.getApproxValue())
                .ownsAsset(asset.getOwnsAsset())
                .homeLoanCompany(asset.getHomeLoanCompany())
                .hasHomeLoan(asset.getHasHomeLoan())
                .createdBy(asset.getCreatedBy())
                .updatedBy(asset.getUpdatedBy())
                .build();
    }

    public AdditionalInfoCustomerDto mapToAdditionalInfoCustomerDto(Customer customer) {
        if (customer == null) return null;
        return AdditionalInfoCustomerDto.builder()
                .nationality(customer.getNationality())
                .preferredLanguageId(customer.getPreferredLanguageId())
                .residentialStatusId(customer.getResidentialStatusId())
                .build();
    }



    public CustomerAdditionalInfoResponseDto buildResponseDto(
            Customer customer,
            CustomerEmployment employment,
            CustomerReferral referral,
            CustomerPep pep,
            CustomerProfileExtra profileExtra,
            CustomerAsset assets) {

        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerAdditionalInfoResponseDto.AdditionalInfoDto additional =
                CustomerAdditionalInfoResponseDto.AdditionalInfoDto.builder()
                        .employment(mapToEmploymentDto(employment))
                        .referrals(mapToReferralDto(referral))
                        .pep(mapToPepDto(pep))
                        .profileExtra(mapToProfileExtraDto(profileExtra))
                        .assets(mapToAssetDto(assets))
                        .additionalInfoCustomerDto(mapToAdditionalInfoCustomerDto(customer))
                        .build();

        return CustomerAdditionalInfoResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status(customer.getOnboardingStatus())
                .additional(additional)
                .build();
    }
}
