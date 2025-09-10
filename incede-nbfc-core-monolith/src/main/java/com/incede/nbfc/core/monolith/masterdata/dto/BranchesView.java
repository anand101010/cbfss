package com.incede.nbfc.core.monolith.masterdata.dto;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface BranchesView {

    String getBranchCode();
    String getBranchName();
    String getBranchShortName();
    BranchTypes getBranchType();
    Integer getStatus();
    Branches getParentBranch();
    String getAdminUnitType();
    String getParentAdminCode();
    String getLocationCode();
    LocalDate getOpeningDate();
    LocalDate getClosingDate();
    LocalDate getDateOfShift();
    Integer getNumExtensionCounters();
    Boolean getIsMainBranchInLocation();
    Branches getLinkServiceMainBranch();
    Short getNumSplitPremises();
    Boolean getLocalClearingMember();
    Boolean getNationalClearingMember();
    Boolean getHighValueClearingMember();
    Short getNumOfficersAvailable();
    String getMicrCode();
    String getIfscCode();
    String getSwiftBicCode();
    String getBsrCode();
    Boolean getClearingBasedOnMicr();
    Boolean getCashMgmtBranch();
    Boolean getRtgsDepEnabled();
    Boolean getAuthDealForex();
    Boolean getAuthForeignCurrencyDeposit();
    Boolean getDdIssueAllowed();
    Boolean getTtIssueAllowed();
    Character getBaseCurrencyCode();
    String getAuthDealerCode();
    String getTbaMainKey();
    String getRegDirectoryCode();
    String getDedicatedIssueOperations();
    String getDoorNumber();
    String getAddressLine1();
    String getAddressLine2();
    String getLandmark();
    String getPlaceName();
    PostOffices getPostOffice();
    Cities getCity();
    Districts getDistrict();
    States getState();
    Countries getCountry();
    Integer getPincode();
    BigDecimal getLatitude();
    BigDecimal getLongitude();
    String getTimezone();
    UUID getIdentity();



}
