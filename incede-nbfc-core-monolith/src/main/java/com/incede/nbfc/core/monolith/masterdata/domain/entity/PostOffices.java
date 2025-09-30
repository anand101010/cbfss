package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "post_offices", schema = "master_data")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostOffices extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_office_id")
    private Integer postOfficeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pincode_id", nullable = false)
    private Pincodes pincode;

    @Column(name = "office_name", nullable = false, length = 100)
    private String officeName;

    @Column(name = "office_type", nullable = false, length = 30)
    private String officeType;

    @Column(name = "delivery_status", length = 20)
    private String deliveryStatus;

    @Column(name = "taluk", length = 80)
    private String taluk;

    @Column(name = "region", length = 80)
    private String region;

    @Column(name = "division", length = 80)
    private String division;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}
