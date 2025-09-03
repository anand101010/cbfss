package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.bouncycastle.util.encoders.Base64Encoder;

/**
 * Entity representing customer scores such as loyalty or value metrics.
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer_scores", schema = "customers")
public class CustomerScore extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Integer scoreId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference must not be null")
    private Customer customer;

    @Column(name = "score_type", length = 20, nullable = false)
    @NotBlank(message = "Score type must not be blank")
    @Size(max = 20, message = "Score type must not exceed 20 characters")
    private String scoreType;

    @Digits(integer = 10, fraction = 2)
    @Column(name = "score_value", precision = 10, scale = 2, nullable = false)
    @NotNull(message = "Score value must not be null")
    private BigDecimal scoreValue;

    @Column(name = "as_of", nullable = false)
    @NotNull(message = "As of must not be null")
    @PastOrPresent(message = "As of timestamp must be in the past or present")
    private LocalDate asOf;

    @Column(name = "source", length = 50)
    @Size(max = 50, message = "Source must not exceed 50 characters")
    private String source;


}