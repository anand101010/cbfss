package com.incede.nbfc.core.monolith.customer.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Branches;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentMaster;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;



@Entity
@Table(name = "customer_form60", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerForm60 extends CustomerBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form60_id")
    private Integer form60Id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customerId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "branch_id", referencedColumnName = "branch_id")
    private Branches branchId;

    @Column(name = "transaction_amount", precision = 15, scale = 2)
    private BigDecimal transactionAmount;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "mode_of_transaction", length = 30)
    private String modeOfTransaction;

    @Column(name = "no_of_persons")
    private Integer numberOfPersons = 1;

    @Column(name = "agricultural_income", precision = 15, scale = 2)
    private BigDecimal agriculturalIncome;

    @Column(name = "other_income", precision = 15, scale = 2)
    private BigDecimal otherIncome;

    @Column(name = "taxable_income", precision = 15, scale = 2)
    private BigDecimal taxableIncome;

    @Column(name = "non_taxable_income", precision = 15, scale = 2)
    private BigDecimal nonTaxableIncome;

    @Column(name = "pan_card_application_date")
    private LocalDate panCardApplicationDate;

    @Column(name = "pan_card_application_ackno", length = 20)
    private String panCardApplicationAckNo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "pid_document_id", referencedColumnName = "doc_id")
    private DocumentMaster pidDocument;

    @Column(name = "pid_document_no", length = 50)
    private String pidDocumentNo;

    @Column(name = "pid_issuing_authority", length = 50)
    private String pidIssuingAuthority;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "add_document_id", referencedColumnName = "doc_id")
    private DocumentMaster addDocument;

    @Column(name = "add_document_no", length = 50)
    private String addDocumentNo;

    @Column(name = "add_issuing_authority", length = 50)
    private String addIssuingAuthority;

    @Column(name = "submission_date")
    private LocalDate submissionDate;

    @Column(name = "form_file_id")
    private Integer formFileId;

    @Column(name = "masked_adhar", length = 50)
    private String maskedAdhar;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "floor_number")
    private String floorNumber;

    @Column(name = "name_of_premises")
    private String nameOfPremises;

}
