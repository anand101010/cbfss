package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pincodes", schema = "master_data")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pincodes extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pincode_id")
    private Integer pincodeId;

    @Column(name = "city_id", nullable = false, length = 50)
    private Integer cityId;

    @Column(name = "district_id",nullable = false, length = 50)
    private Integer districtId;

    @Column(name = "state_id", nullable = false, length = 50)
    private Integer stateId;

    @Column(name = "pincode", nullable = false, length = 6)
    private Integer pincode;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
}
