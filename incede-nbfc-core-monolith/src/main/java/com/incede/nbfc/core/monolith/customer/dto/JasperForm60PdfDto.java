package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JasperForm60PdfDto {

    private String firstName;
    private String middleName;
    private String surname;
    private String dateOfBirth;
    private String fatherFirstName;
    private String fatherMiddleName;
    private String fatherSurname;
    private String flatNumber;
    private String floorNumber;
    private String nameOfPremises;
    private String blockName;
    private String roadName;
    private String areaName;
    private String cityName;
    private String districtName;
    private String stateName;
    private String pinCode;
    private String telephoneNumber;
    private String mobileNumber;
    private String taxPayerNumber;
    private String amountOfTransaction;
    private String dateOfTransaction;
    private String modeOfTransaction;
    private String adharNumber;
    private String dateOfPanGenerated;
    private String panAcknowledgementNumber;
    private String noPanAgriculturalIncome;
    private String noPanOtherThanAgriculturalIncome;
    private String noPanIncomeChargeableTaxForFC;
    private String noPanIncomeNotChargeableTaxForFC;
    private String applicationFor;
    private String designation;
    private String verifiedDay;
    private String place;
    private String verifiedYearLastDigits;
}
