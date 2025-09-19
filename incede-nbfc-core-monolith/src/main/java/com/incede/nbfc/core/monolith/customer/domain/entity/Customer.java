package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import lombok.ToString;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Customer entity for the NBFC system.
 *
 * @author Incede NBFC Development Team
 * @version 1.0.0

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
    @NotNull(message = "Tenant ID is required")
    private Integer tenantId;

    @Column(name = "customer_code", nullable = false, length = 15)
    @NotBlank(message = "Customer code must not be blank")
    @Size(max = 15, message = "Customer code must not exceed 15 characters")
    private String customerCode;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotBlank(message = "First name must not be blank")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @Column(name = "middle_name", length = 50)
    @Size(max = 50, message = "Middle name must not exceed 50 characters")
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank(message = "Last name must not be blank")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Column(name = "display_nme", nullable = false, length = 100)
    @NotBlank(message = "Display name must not be blank")
    @Size(max = 100, message = "Display name must not exceed 100 characters")
    private String displayName;

    @NotNull(message = "BranchId is required")
    private Integer branchId;

    @Column(name = "salutation")
    @Min(value = 1, message = "salutation must be a positive integer")
    @Max(value = 5, message = "salutation should not exceed 5")
    private Integer salutation;

    @Column(name = "gender")
    @Min(value = 1, message = "gender must be a positive integer")
    private Integer gender;

    @Column(name = "dob", nullable = false)
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Column(name = "marital_status")
    @Min(value = 1, message = "Marital Status must be a positive integer")
    @Max(value = 4, message = "Martial Status must not be greater than 4.")
    private Integer maritalStatus;


    @Column(name = "nationality")
    @Min(value = 1, message = "Nationality should be a positive integer")
    private Integer nationality = 1;


    @Column(name = "tax_category", nullable = false)
    @NotNull(message = "Tax category is required")
    @Min(value = 1, message = "Tax Category should be a positive integer")
    private Integer taxCategory;

    @Column(name = "occupation", nullable = false)
    @Min(value = 1, message = "occupation should be a positive integer")
    private Integer occupation;

    @ToString.Exclude

    @Column(name = "employer", length = 100)
    @Size(max = 100, message = "Employer name must not exceed 100 characters")
    private String employer;

    @ToString.Exclude
    @Column(name = "annual_income", precision = 15, scale = 2)
    @Digits(integer = 13, fraction = 2, message = "Annual income must be a valid monetary amount")
    private BigDecimal annualIncome;

    @Column(name = "customer_status", nullable = false)
    @NotNull(message = "Customer status is required")
    @Min(value = 1, message = "CustomerStatus must be a positive Integer")
    private Integer customerStatus = 1;

    @ToString.Exclude
    @Column(name = "crm_reference_id", nullable = false, length = 100)
    @Size(max = 100, message = "CRM reference ID must not exceed 100 characters")
    private String crmReferenceId;


    @Column(name = "preferred_language_id", nullable = false)
    private Integer preferredLanguageId;


    @Column(name = "residential_status_id", nullable = false)
    private Integer residentialStatusId;


    @ToString.Exclude
    @Column(name = "onboarding_status", length = 100)
    private String onboardingStatus;

    @ToString.Exclude
    @NotBlank(message = "fatherName cannot be Blank")
    @Column(name = "father_name", length = 100)
    private String fatherName;

    @ToString.Exclude
    @NotBlank(message = "motherName cannot be Blank")
    @Column(name = "mother_name", length = 100)
    private String motherName;

    @ToString.Exclude
    @Column(name = "spouse_name", length = 100)
    private String spouseName;

    @Column(name = "is_firm")
    @NotNull(message = "isFirm should not be Null")
    private Boolean isFirm;

    @Column(name = "is_business")
    @NotNull(message = "isBusiness should not be Null")
    private Boolean isBusiness;

    @Column(name = "isMinor")
    @NotNull(message = "isMinor should not be Null")
    private Boolean isMinor;

    @Column(name="guardian_customer_id")
    private Integer guardianCustomerId;

    @Column(name ="aadhar_vault_id")
    private String aadharVaultId;

    @Column(name="mobile_number")
    private String mobileNumber;

    @Column(name="otp_is_verified")
    private Boolean otpIsVerified;





}