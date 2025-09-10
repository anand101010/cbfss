package com.incede.nbfc.core.monolith.client.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KycResult {

    // Common PAN fields
    @ToString.Exclude
    private String idNumber;
    private String idStatus;
    private String category;
    private String name;

    // Driving License fields
    private List<KycDrivingLicenseAddress> addresses;
    private List<String> allClassOfVehicle;
    private String drivingLicenseNumber;
    private String dateOfBirth;
    private String endorseDate;
    private String endorseNumber;
    private String fatherOrHusbandName;
    private String validFrom;
    private String validTo;

    @JsonProperty("status")
    private String status; // renamed from "status" in DL JSON

    // Voter ID fields
    private String epicNo;
    private String nameInVernacular;
    private String gender;
    private Integer age;
    private String relativeName;
    private String relativeNameInVernacular;
    private String relativeRelationType;
    private String houseNumber;
    private String partOrLocationInConstituency;
    private Integer partNumberOrLocationNumberInConstituency;
    private String parliamentaryConstituency;
    private String assemblyConstituency;
    private Integer sectionOfConstituencyPart;
    private Integer cardSerialNumberInPollingList;
    private String lastUpdateDate;



    private KycPollingBoothDetails pollingBoothDetails;
    private KycVoterAddress address;
    private String emailId;
    private String mobileNumber;
    private String voterDateOfBirth;
    private String district;
    private String state;
    private String stateCode;
    private String pollingBoothCoordinates;
    private String pollingBoothAddress;
    private String pollingBoothNumber;
    private String id; // from voter JSON
}
