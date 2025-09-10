package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerAdditionalInfoMapper {

    // ===================== Create (mapToX) =====================

    public CustomerAsset mapToAsset(CustomerAssetDto assetDto, Customer customer) {
        CustomerAsset asset = new CustomerAsset();
        updateAsset(asset, assetDto, customer);
        return asset;
    }

    public CustomerProfileExtra mapToProfileExtra(CustomerProfileExtraDto profileExtraDto, Customer customer) {
        CustomerProfileExtra profileExtra = new CustomerProfileExtra();
        updateProfileExtra(profileExtra, profileExtraDto, customer);
        return profileExtra;
    }

    public CustomerPep mapToPep(CustomerPepDto pepDto, Customer customer) {
        CustomerPep pep = new CustomerPep();
        updatePep(pep, pepDto, customer);
        return pep;
    }

    public CustomerReferral mapToReferral(CustomerReferralDto referralDto, Customer customer) {
        CustomerReferral referral = new CustomerReferral();
        updateReferral(referral, referralDto, customer);
        return referral;
    }

    public CustomerEmployment mapToEmployment(CustomerEmploymentDto employmentDto, Customer customer) {
        CustomerEmployment employment = new CustomerEmployment();
        updateEmployment(employment, employmentDto, customer);
        return employment;
    }

    // ===================== Update (updateX) =====================

    public void updateAsset(CustomerAsset asset, CustomerAssetDto assetDto, Customer customer) {
        if (assetDto == null) return;
        asset.setCustomer(customer);
        asset.setAssetId(assetDto.getAssetId());
        asset.setAssetTypeId(assetDto.getAssetTypeId());
        asset.setDescription(assetDto.getDescription());
        asset.setApproxValue(assetDto.getApproxValue());
        asset.setOwnsAsset(assetDto.getOwnsAsset());
        asset.setHasHomeLoan(assetDto.getHasHomeLoan());
        asset.setCreatedBy(assetDto.getCreatedBy());
        asset.setUpdatedBy(assetDto.getUpdatedBy());
    }

    public void updateProfileExtra(CustomerProfileExtra profileExtra, CustomerProfileExtraDto profileExtraDto, Customer customer) {
        if (profileExtraDto == null) return;
        profileExtra.setCustomer(customer);
        profileExtra.setEducationLevelId(profileExtraDto.getEducationLevelId());
        profileExtra.setPurposeId(profileExtraDto.getPurposeId());
        profileExtra.setNotes(profileExtraDto.getNotes());
        profileExtra.setCreatedBy(profileExtraDto.getCreatedBy());
        profileExtra.setUpdatedBy(profileExtraDto.getUpdatedBy());
    }

    public void updatePep(CustomerPep pep, CustomerPepDto pepDto, Customer customer) {
        if (pepDto == null) return;
        pep.setCustomer(customer);
        pep.setStatus(pepDto.getStatus());
        pep.setCategoryId(pepDto.getCategoryId());
        pep.setRelationshipId(pepDto.getRelationshipId());
        pep.setVerificationSourceId(pepDto.getVerificationSourceId());
        pep.setCreatedBy(pepDto.getCreatedBy());
        pep.setUpdatedBy(pepDto.getUpdatedBy());
    }

    public void updateReferral(CustomerReferral referral, CustomerReferralDto referralDto, Customer customer) {
        if (referralDto == null) return;
        referral.setCustomer(customer);
        referral.setReferralSourceId(referralDto.getReferralSourceId());
        referral.setCanvassedTypeId(referralDto.getCanvassedTypeId());
        referral.setCanvasserStaffId(referralDto.getCanvasserStaffId());
        referral.setCreatedBy(referralDto.getCreatedBy());
        referral.setUpdatedBy(referralDto.getUpdatedBy());
    }

    public void updateEmployment(CustomerEmployment employment, CustomerEmploymentDto employmentDto, Customer customer) {
        if (employmentDto == null) return;
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

    // ===================== Entity -> DTO =====================

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
                .notes(profileExtra.getNotes())
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
                .hasHomeLoan(asset.getHasHomeLoan())
                .createdBy(asset.getCreatedBy())
                .updatedBy(asset.getUpdatedBy())
                .build();
    }

    // ===================== Build Response DTO =====================

    public CustomerAdditionalInfoResponseDto buildResponseDto(
            Customer customer,
            CustomerEmployment employment,
            CustomerReferral referral,
            CustomerPep pep,
            CustomerProfileExtra profileExtra,
            CustomerAsset assets) {

        CustomerAdditionalInfoResponseDto.AdditionalInfoDto additional =
                CustomerAdditionalInfoResponseDto.AdditionalInfoDto.builder()
                        .employment(mapToEmploymentDto(employment))
                        .referrals(mapToReferralDto(referral))
                        .pep(mapToPepDto(pep))
                        .profileExtra(mapToProfileExtraDto(profileExtra))
                        .assets(assets.stream().map(this::mapToAssetDto).collect(Collectors.toList()))
                        .build();

        return CustomerAdditionalInfoResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status(customer.getOnboardingStatus())
                .additional(additional)
                .build();
    }
}
