package com.incede.nbfc.core.monolith.lead.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressProofType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.PostOffices;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "lead_addresses", schema = "lead")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadAddress extends LeadBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false, referencedColumnName = "lead_id")
    @NotNull(message = "Lead reference must not be null")
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_type_id", nullable = false, referencedColumnName = "address_type_id")
    private AddressType addressType;

    @Column(name = "house_no", length = 50)
    private String houseNo;

    @Column(name = "street_name", length = 100)
    private String streetName;

    @Column(name = "place_name", length = 100)
    private String placeName;

    @Column(name = "landmark", length = 100)
    private String landmark;

    @Column(name = "pincode")
    @Min(value = 100000, message = "Pincode must be >= 100000")
    @Max(value = 999999, message = "Pincode must be <= 999999")
    private Integer pincode;

    @Column(name = "country_id")
    private String  country;

    @Column(name = "state_id")
    private String  state;

    @Column(name = "district_id")
    private String  district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_office_id", nullable = false, referencedColumnName = "post_office_id")
    private PostOffices postOfficeId;

    @Column(name = "city_id")
    private String  city;

    @Column(name = "digipin", length = 20, unique = true)
    private String digipin;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_proof_type_id", nullable = false, referencedColumnName = "address_proof_type_id")
    private AddressProofType addressProofType;

    @Column(name = "document_ref_id")
    private Integer documentRefId;

    @Column(name = "document_path", length = 255)
    private String documentPath;
}
