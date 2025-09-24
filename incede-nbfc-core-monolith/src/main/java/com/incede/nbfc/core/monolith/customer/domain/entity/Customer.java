package com.incede.nbfc.core.monolith.customer.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Customer entity for the NBFC system.
 */
@Entity
@Table(name = "customer", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends CustomerBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "tenant_id", nullable = false)
    @NotNull(message = "Tenant ID is mandatory")
    private Integer tenantId;

    @Column(name = "customer_code", nullable = false, length = 15)
    @NotBlank(message = "Customer code is required")
    @Size(max = 15, message = "Customer code must not exceed 15 characters")
    private String customerCode;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @Column(name = "middle_name", length = 50)
    @Size(max = 50, message = "Middle name must not exceed 50 characters")
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Column(name = "display_nme", length = 100)
    @Size(max = 100, message = "Display name must not exceed 100 characters")
    private String displayName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "branch_id", referencedColumnName = "branch_id")
    private Branches branchId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "salutation", referencedColumnName = "salutation_id")
    private SalutationTypes salutation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "gender", referencedColumnName = "gender_id")
    private Genders gender;

    @Column(name = "dob", nullable = false)
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "marital_status", referencedColumnName = "status_id")
    private MaritalStatus maritalStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "nationality", referencedColumnName = "nationality_id")
    private Nationality nationality;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tax_category", referencedColumnName = "tax_cat_id")
    private TaxCategory taxCategory;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "occupation", referencedColumnName = "occupation_id")
    private Occupation occupation;

    @Column(name = "employer", length = 100)
    @Size(max = 100, message = "Employer name must not exceed 100 characters")
    private String employer;

    @Column(name = "annual_income", precision = 15, scale = 2)
    @PositiveOrZero(message = "Annual income must be zero or a positive value")
    private BigDecimal annualIncome;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_status", referencedColumnName = "status_id")
    private CustomerStatus customerStatus;

    @Column(name = "crm_reference_id", length = 100)
    @Size(max = 100, message = "CRM reference ID must not exceed 100 characters")
    private String crmReferenceId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "preferred_language_id", referencedColumnName = "language_id")
    private Languages preferredLanguageId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "residential_status_id", referencedColumnName = "residential_status_id")
    private ResidentialStatuses residentialStatusId;

    @Column(name = "onboarding_status", length = 100)
    private String onboardingStatus;

    @Column(name = "father_name", length = 100)
    private String fatherName;

    @Column(name = "mother_name", length = 100)
    private String motherName;

    @Column(name = "spouse_name", length = 100)
    private String spouseName;

    @Column(name = "is_firm")
    @NotNull(message = "Firm status is required")
    private Boolean isFirm;

    @Column(name = "is_business")
    @NotNull(message = "Business status is required")
    private Boolean isBusiness;

    @Column(name = "is_minor")
    @NotNull(message = "Minor status is required")
    private Boolean isMinor;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "guardian_customer_id", referencedColumnName = "customer_id")
    private Customer guardianCustomer;

    @Column(name = "aadhar_vault_id")
    private String aadharVaultId;

    @Column(name = "mobile_number")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Column(name = "otp_is_verified")
    private Boolean otpIsVerified;
}
