package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * Occupation Entity for Master Data Management.
 *
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */

@Entity
@Table(name = "occupations", schema = "master_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Occupation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "occupation_id")
    private Integer occupationId;

    @Column(name = "occupation_name", nullable = false, length = 100)
    private String occupationName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;

}