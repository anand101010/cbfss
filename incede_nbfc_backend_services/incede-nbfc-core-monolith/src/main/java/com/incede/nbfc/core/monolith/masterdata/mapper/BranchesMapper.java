package com.incede.nbfc.core.monolith.masterdata.mapper;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Branches;
import com.incede.nbfc.core.monolith.masterdata.dto.BranchesDto;
import org.springframework.stereotype.Component;

@Component
public class BranchesMapper {

    public BranchesDto convertToDto(Branches branch) {
        BranchesDto dto = new BranchesDto();
        dto.setBranchId(branch.getBranchId());
        dto.setTenantId(branch.getTenantId());
        dto.setBranchCode(branch.getBranchCode());
        dto.setBranchName(branch.getBranchName());
        dto.setBranchShortName(branch.getBranchShortName());
        dto.setBranchTypeId(branch.getBranchTypeId());
        dto.setStatusId(branch.getStatusId());
        dto.setParentBranchId(branch.getParentBranchId());
        dto.setAdminUnitType(branch.getAdminUnitType());
        dto.setParentAdminCode(branch.getParentAdminCode());
        dto.setLocationCode(branch.getLocationCode());
        dto.setOpeningDate(branch.getOpeningDate());
        dto.setClosingDate(branch.getClosingDate());
        dto.setDateOfShift(branch.getDateOfShift());
        dto.setCategoryId(branch.getCategoryId());
        dto.setSizeId(branch.getSizeId());
        dto.setNumExtensionCounters(branch.getNumExtensionCounters());
        dto.setIsMainBranchInLocation(branch.getIsMainBranchInLocation());
        dto.setLinkServiceMainBranchId(branch.getLinkServiceMainBranchId());
        dto.setIsSplitPremises(branch.getIsSplitPremises());
        dto.setNumSplitPremises(branch.getNumSplitPremises());
        dto.setLocalClearingMember(branch.getLocalClearingMember());
        dto.setNationalClearingMember(branch.getNationalClearingMember());
        dto.setHighValueClearingMember(branch.getHighValueClearingMember());
        dto.setNumOfficersAvailable(branch.getNumOfficersAvailable());
        dto.setMicrCode(branch.getMicrCode());
        dto.setIfscCode(branch.getIfscCode());
        dto.setSwiftBicCode(branch.getSwiftBicCode());
        dto.setBsrCode(branch.getBsrCode());
        dto.setClearingBasedOnMicr(branch.getClearingBasedOnMicr());
        dto.setCashMgmtBranch(branch.getCashMgmtBranch());
        dto.setRtgsDepEnabled(branch.getRtgsDepEnabled());
        dto.setAuthDealForex(branch.getAuthDealForex());
        dto.setAuthForeignCurrencyDeposit(branch.getAuthForeignCurrencyDeposit());
        dto.setDdIssueAllowed(branch.getDdIssueAllowed());
        dto.setTtIssueAllowed(branch.getTtIssueAllowed());
        dto.setBaseCurrencyCode(branch.getBaseCurrencyCode());
        dto.setAuthDealerCode(branch.getAuthDealerCode());
        dto.setTbaMainKey(branch.getTbaMainKey());
        dto.setRegDirectoryCode(branch.getRegDirectoryCode());
        dto.setDedicatedIssueOperations(branch.getDedicatedIssueOperations());
        dto.setDoorNumber(branch.getDoorNumber());
        dto.setAddressLine1(branch.getAddressLine1());
        dto.setAddressLine2(branch.getAddressLine2());
        dto.setLandmark(branch.getLandmark());
        dto.setPlaceName(branch.getPlaceName());
        dto.setPostOfficeId(branch.getPostOfficeId());
        dto.setCityId(branch.getCityId());
        dto.setDistrictId(branch.getDistrictId());
        dto.setStateId(branch.getStateId());
        dto.setCountryId(branch.getCountryId());
        dto.setPincodeId(branch.getPincodeId());
        dto.setLatitude(branch.getLatitude());
        dto.setLongitude(branch.getLongitude());
        dto.setTimezone(branch.getTimezone());
        dto.setIdentity(branch.getIdentity());
        return dto;
    }

}
