package com.incede.nbfc.core.monolith.masterdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchesDto {

    private Integer branchId;
    private Integer tenantId;
    private String branchCode;
    private String branchName;
    private String branchShortName;
    private Integer branchTypeId;
    private Integer statusId;
    private Integer parentBranchId;
    private String adminUnitType;
    private String parentAdminCode;
    private String locationCode;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private LocalDate dateOfShift;
    private Integer categoryId;
    private Integer sizeId;
    private Integer numExtensionCounters;
    private Boolean isMainBranchInLocation = false;
    private Integer linkServiceMainBranchId;
    private Boolean isSplitPremises = false;
    private Short numSplitPremises;
    private Boolean localClearingMember = false;
    private Boolean nationalClearingMember = false;
    private Boolean highValueClearingMember = false;
    private Short numOfficersAvailable;
    private String micrCode;
    private String ifscCode;
    private String swiftBicCode;
    private String bsrCode;
    private Boolean clearingBasedOnMicr = false;
    private Boolean cashMgmtBranch = false;
    private Boolean rtgsDepEnabled = false;
    private Boolean authDealForex = false;
    private Boolean authForeignCurrencyDeposit = false;
    private Boolean ddIssueAllowed = false;
    private Boolean ttIssueAllowed = false;
    private Character baseCurrencyCode;
    private String authDealerCode;
    private String tbaMainKey;
    private String regDirectoryCode;
    private String dedicatedIssueOperations;
    private String doorNumber;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String placeName;
    private Integer postOfficeId;
    private Integer cityId;
    private Integer districtId;
    private Integer stateId;
    private Integer countryId;
    private Integer pincodeId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String timezone = "Asia/Kolkata";
    private UUID identity;
    private StatesDto stateDto;
    private BranchTypeDto branchTypeDto;
    private StatusDto statusDto;
    private PostOfficesDto postOfficesDto;
    private CitiesDto CitiesDto;
    private DistrictDto DistrictDto;

}
