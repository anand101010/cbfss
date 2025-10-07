package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.dto.*;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class JasperForm60Mapper {

    private String toWholeNumberString(BigDecimal val) {
        return val != null ? val.setScale(0, RoundingMode.HALF_UP).toString() : null;
    }

    private String formatDateToDDMMYYYY(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("ddMMyyyy")) : null;
    }

    private String safeToString(Object obj) {
        return obj != null ? obj.toString() : null;
    }

    private String safeFormatDate(LocalDate date) {
        return Optional.ofNullable(date)
                .map(d -> d.format(DateTimeFormatter.ofPattern("ddMMyyyy")))
                .orElse(null);
    }

    public static String getCurrentFormattedDate() {
        LocalDate today = LocalDate.now();
        String month = today.getMonth()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                .toUpperCase();
        int day = today.getDayOfMonth();
        String suffix = getDayOfMonthSuffix(day);
        return month + " " + day + suffix;
    }

    private static String getDayOfMonthSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "TH";
        }
        return switch (day % 10) {
            case 1 -> "ST";
            case 2 -> "ND";
            case 3 -> "RD";
            default -> "TH";
        };
    }

    public Map<String, Object> form60ToJasperDto(CustomerForm60ResponseDto dto,
                                                 Customer customer,
                                                 CustomerAddressDetailDto permanentAddress,
                                                 CustomerPurposeResponseDto customerPurpose,
                                                 CustomerDesignationResponseDto designation,
                                                 String branchPlace) {

        Objects.requireNonNull(dto, "dto must not be null");
        Objects.requireNonNull(customer, "customer must not be null");
        Objects.requireNonNull(permanentAddress, "permanentAddress must not be null");
        Objects.requireNonNull(customerPurpose, "customerPurpose must not be null");
        Objects.requireNonNull(designation, "designation must not be null");
        Objects.requireNonNull(branchPlace, "branchPlace must not be null");

        int currentYear = LocalDate.now().getYear();
        Map<String, Object> params = new HashedMap<>();

//        params.put("fatherMiddleName", null);
//        params.put("fatherSurname", null);
//        params.put("taxPayerNumber", null);
        params.put("firstName", safeToString(customer.getFirstName()));
        params.put("middleName", safeToString(customer.getMiddleName()));
        params.put("Surname", safeToString(customer.getLastName()));
        params.put("dateOfBirth", safeFormatDate(customer.getDob()));
        params.put("fatherFirstName", safeToString(customer.getFatherName()));
        params.put("flatNumber", safeToString(permanentAddress.getDoorNumber()));
        params.put("floorNumber", safeToString(dto.getFloorNumber()));
        params.put("nameOfPremises", safeToString(dto.getNameOfPremises()));
        params.put("blockName", safeToString(permanentAddress.getPlaceName()));
        params.put("roadName", safeToString(permanentAddress.getAddressLine1()));
        params.put("areaName", safeToString(permanentAddress.getAddressLine2()));
        params.put("cityName", safeToString(permanentAddress.getCity()));
        params.put("districtName", safeToString(permanentAddress.getDistrict()));
        params.put("stateName", safeToString(permanentAddress.getState()));
        params.put("pinCode", safeToString(permanentAddress.getPincode()));
        params.put("telephoneNumber", safeToString(dto.getTelephoneNumber()));
        params.put("mobileNumber", safeToString(customer.getMobileNumber()));
        params.put("amountOfTransaction", toWholeNumberString(dto.getTransactionAmount()));
        params.put("dateOfTransaction", safeFormatDate(dto.getTransactionDate()));
        params.put("modeOfTransaction", safeToString(dto.getModeOfTransaction()));
        params.put("adharNumber", safeToString(dto.getMaskedAdhar()));
        params.put("dateOfPanGenerated", safeFormatDate(dto.getPanCardApplicationDate()));
        params.put("panAcknowledgementNumber", safeToString(dto.getPanCardApplicationAckNo()));
        params.put("noPanAgriculturalIncome", toWholeNumberString(dto.getAgriculturalIncome()));
        params.put("noPanOtherThanAgriculturalIncome", toWholeNumberString(dto.getOtherIncome()));
        params.put("noPanIncomeChargeableTaxForFC", toWholeNumberString(dto.getTaxableIncome()));
        params.put("noPanIncomeNotChargeableTaxForFC", toWholeNumberString(dto.getNonTaxableIncome()));
        params.put("applicationFor", safeToString(customerPurpose.getName()));
        params.put("designation", safeToString(designation.getName()));
        params.put("verifiedDay", getCurrentFormattedDate());
        params.put("place", safeToString(branchPlace));
        params.put("verifiedYearLastDigits", String.valueOf(currentYear));

        return params;
    }
}

