package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "branches", schema = "master_data")
public class Branches extends BaseEntity implements  Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="branch_id", nullable = false, length = 20)
    private Integer branchId;


    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "branch_code", nullable = false, unique = true, length = 10)
    private String branchCode;

    @Column(name = "branch_name", nullable = false, length = 100)
    private String branchName;

    @Column(name = "branch_short_name", length = 50)
    private String branchShortName;

    @Column(name = "branch_type_id",nullable = false, length = 50 )
    private Integer branchTypeId;

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "parent_branch_id", nullable = false, length = 50)
    private Integer parentBranchId;

    @Column(name = "admin_unit_type", length = 30)
    private String adminUnitType;

    @Column(name = "parent_admin_code", length = 30)
    private String parentAdminCode;

    @Column(name = "location_code", length = 30)
    private String locationCode;

    @Column(name = "opening_date")
    private LocalDate openingDate;

    @Column(name = "closing_date")
    private LocalDate closingDate;

    @Column(name = "date_of_shift")
    private LocalDate dateOfShift;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "size_id")
    private Integer sizeId;

    @Column(name = "num_extension_counters")
    private Integer numExtensionCounters;

    @Column(name = "is_main_branch_in_location")
    private Boolean isMainBranchInLocation = false;

    @Column(name = "link_service_main_branch_id", nullable = false, length = 50)
    private Integer linkServiceMainBranchId;

    @Column(name = "is_split_premises")
    private Boolean isSplitPremises = false;

    @Column(name = "num_split_premises")
    private Short numSplitPremises;

    @Column(name = "local_clearing_member")
    private Boolean localClearingMember = false;

    @Column(name = "national_clearing_member")
    private Boolean nationalClearingMember = false;

    @Column(name = "high_value_clearing_member")
    private Boolean highValueClearingMember = false;

    @Column(name = "num_officers_available")
    private Short numOfficersAvailable;

    @Column(name = "micr_code", length = 20)
    private String micrCode;

    @Column(name = "ifsc_code", length = 20)
    private String ifscCode;

    @Column(name = "swift_bic_code", length = 20)
    private String swiftBicCode;

    @Column(name = "bsr_code", length = 20)
    private String bsrCode;

    @Column(name = "clearing_based_on_micr")
    private Boolean clearingBasedOnMicr = false;

    @Column(name = "cash_mgmt_branch")
    private Boolean cashMgmtBranch = false;

    @Column(name = "rtgs_dep_enabled")
    private Boolean rtgsDepEnabled = false;

    @Column(name = "auth_deal_forex")
    private Boolean authDealForex = false;

    @Column(name = "auth_foreign_currency_deposit")
    private Boolean authForeignCurrencyDeposit = false;

    @Column(name = "dd_issue_allowed")
    private Boolean ddIssueAllowed = false;

    @Column(name = "tt_issue_allowed")
    private Boolean ttIssueAllowed = false;

    @Column(name = "base_currency_code", length = 3)
    private Character baseCurrencyCode;

    @Column(name = "auth_dealer_code", length = 20)
    private String authDealerCode;

    @Column(name = "tba_main_key", length = 50)
    private String tbaMainKey;

    @Column(name = "reg_directory_code", length = 30)
    private String regDirectoryCode;

    @Column(name = "dedicated_issue_operations", length = 10)
    private String dedicatedIssueOperations;

    @Column(name = "door_number", length = 50)
    private String doorNumber;

    @Column(name = "address_line1", length = 100)
    private String addressLine1;

    @Column(name = "address_line2", length = 100)
    private String addressLine2;

    @Column(name = "landmark", length = 100)
    private String landmark;

    @Column(name = "place_name", length = 100)
    private String placeName;

    @Column(name = "post_office_id", nullable = false, length = 50)
    private Integer postOfficeId;

    @Column(name = "city_id", nullable = false, length = 50)
    private Integer cityId;

    @Column(name = "district_id",nullable = false, length = 50)
    private Integer districtId;

    @Column(name = "state_id", nullable = false, length = 50)
    private Integer stateId;

    @Column(name = "country_id", nullable = false, length = 50)
    private Integer countryId;

    @Column(name = "pincode_id", nullable = false, length = 50)
    private Integer pincodeId;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "timezone", nullable = false, length = 64)
    private String timezone = "Asia/Kolkata";

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}

