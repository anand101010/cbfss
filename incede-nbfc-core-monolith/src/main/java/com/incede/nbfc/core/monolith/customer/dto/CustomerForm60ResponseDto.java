package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerForm60ResponseDto {

    private BigDecimal transactionAmount;
    private LocalDate transactionDate;
    private String modeOfTransaction;
    private Integer numberOfPersons;

    private BigDecimal agriculturalIncome;
    private BigDecimal otherIncome;
    private BigDecimal taxableIncome;
    private BigDecimal nonTaxableIncome;

    private LocalDate panCardApplicationDate;
    private String panCardApplicationAckNo;

    private Integer pidDocumentId;
    private String pidDocumentNo;
    private String pidIssuingAuthority;

    private Integer addDocumentId;
    private String addDocumentNo;
    private String addIssuingAuthority;

    private LocalDate submissionDate;
    private Integer formFileId;

    private String telephoneNumber;
    private String floorNumber;
    private String nameOfPremises;
    private String maskedAdhar;
    private Integer branchId;

    private UUID identity;


}
