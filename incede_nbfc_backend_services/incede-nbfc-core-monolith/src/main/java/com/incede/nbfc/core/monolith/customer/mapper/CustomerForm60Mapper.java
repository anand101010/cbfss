package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerForm60;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60RequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60ResponseDto;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Branches;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentMaster;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class CustomerForm60Mapper {
    public CustomerForm60 toEntity(CustomerForm60RequestDto dto, Customer customer, Branches branch, DocumentMaster pidDoc, DocumentMaster addDoc) {
        CustomerForm60 form60 = new CustomerForm60();
        form60.setCustomerId(customer);
        form60.setCustomerId(customer);
        form60.setBranchId(branch);
        form60.setPidDocument(pidDoc);
        form60.setAddDocument(addDoc);
        form60.setTransactionAmount(dto.getTransactionAmount());
        form60.setTransactionDate(dto.getTransactionDate());
        form60.setModeOfTransaction(dto.getModeOfTransaction());
        form60.setNumberOfPersons(dto.getNumberOfPersons());

        form60.setAgriculturalIncome(dto.getAgriculturalIncome());
        form60.setOtherIncome(dto.getOtherIncome());
        form60.setTaxableIncome(dto.getTaxableIncome());
        form60.setNonTaxableIncome(dto.getNonTaxableIncome());

        form60.setPanCardApplicationDate(dto.getPanCardApplicationDate());
        form60.setPanCardApplicationAckNo(dto.getPanCardApplicationAckNo());

        form60.setPidDocumentNo(dto.getPidDocumentNo());
        form60.setPidIssuingAuthority(dto.getPidIssuingAuthority());

        form60.setAddDocumentNo(dto.getAddDocumentNo());
        form60.setAddIssuingAuthority(dto.getAddIssuingAuthority());

        form60.setSubmissionDate(dto.getSubmissionDate());
        form60.setFormFileId(dto.getFormFileId());
        form60.setCreatedBy(dto.getCreatedBy());
        form60.setNameOfPremises(dto.getNameOfPremises());
        form60.setFloorNumber(dto.getFloorNumber());
        form60.setTelephoneNumber(dto.getTelephoneNumber());

        return form60;
    }

    public CustomerForm60ResponseDto toResponseDto(CustomerForm60 entity) {
        return CustomerForm60ResponseDto.builder()
                .transactionAmount(entity.getTransactionAmount())
                .transactionDate(entity.getTransactionDate())
                .modeOfTransaction(entity.getModeOfTransaction())
                .numberOfPersons(entity.getNumberOfPersons())
                .agriculturalIncome(entity.getAgriculturalIncome())
                .otherIncome(entity.getOtherIncome())
                .taxableIncome(entity.getTaxableIncome())
                .nonTaxableIncome(entity.getNonTaxableIncome())
                .panCardApplicationDate(entity.getPanCardApplicationDate())
                .panCardApplicationAckNo(entity.getPanCardApplicationAckNo())
                .pidDocumentId(entity.getPidDocument() != null ? entity.getPidDocument().getDocId() : null)
                .pidDocumentNo(entity.getPidDocumentNo())
                .pidIssuingAuthority(entity.getPidIssuingAuthority())
                .addDocumentId(entity.getAddDocument() != null ? entity.getAddDocument().getDocId() : null)
                .addDocumentNo(entity.getAddDocumentNo())
                .addIssuingAuthority(entity.getAddIssuingAuthority())
                .submissionDate(entity.getSubmissionDate())
                .formFileId(entity.getFormFileId())
                .identity(entity.getIdentity())
                .telephoneNumber(entity.getTelephoneNumber())
                .nameOfPremises(entity.getNameOfPremises())
                .floorNumber(entity.getFloorNumber())
                .maskedAdhar(entity.getMaskedAdhar())
                .build();
    }

    /**
     * Update an existing CustomerForm60 entity with values from the DTO.
     *
     * @param entity Existing CustomerForm60 entity
     * @param dto    DTO with updated values
     */
    public void updateEntityFromDto(CustomerForm60 entity,
                                    CustomerForm60RequestDto dto,
                                    DocumentMaster pidDocument,
                                    DocumentMaster addDocument) {
        Objects.requireNonNull(dto, "CustomerForm60RequestDto must not be null");

        entity.setTransactionAmount(dto.getTransactionAmount());
        entity.setTransactionDate(dto.getTransactionDate());
        entity.setModeOfTransaction(dto.getModeOfTransaction());
        entity.setNumberOfPersons(dto.getNumberOfPersons());
        entity.setAgriculturalIncome(dto.getAgriculturalIncome());
        entity.setOtherIncome(dto.getOtherIncome());
        entity.setTaxableIncome(dto.getTaxableIncome());
        entity.setNonTaxableIncome(dto.getNonTaxableIncome());
        entity.setPanCardApplicationDate(dto.getPanCardApplicationDate());
        entity.setPanCardApplicationAckNo(dto.getPanCardApplicationAckNo());
        entity.setPidDocument(pidDocument);
        entity.setPidDocumentNo(dto.getPidDocumentNo());
        entity.setPidIssuingAuthority(dto.getPidIssuingAuthority());
        entity.setAddDocument(addDocument);
        entity.setAddDocumentNo(dto.getAddDocumentNo());
        entity.setAddIssuingAuthority(dto.getAddIssuingAuthority());
        entity.setSubmissionDate(dto.getSubmissionDate());
        entity.setFormFileId(dto.getFormFileId());
        entity.setUpdatedBy(dto.getUpdatedBy());
        entity.setTelephoneNumber(dto.getTelephoneNumber());
        entity.setNameOfPremises(dto.getNameOfPremises());
        entity.setFloorNumber(dto.getFloorNumber());
    }


}

