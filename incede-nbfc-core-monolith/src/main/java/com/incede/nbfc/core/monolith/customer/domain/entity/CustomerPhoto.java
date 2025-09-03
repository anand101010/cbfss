package com.incede.nbfc.core.monolith.customer.domain.entity;
import com.incede.nbfc.core.monolith.customer.enums.PhotoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_photo",schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPhoto  extends CustomerBaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer photoId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference must not be null")
    private Customer customer;

    @Column(name = "photo_ref_id", nullable = false)
    @NotNull(message = "Photo reference ID is required")
    @Min(value = 1, message = "Photo reference id must be a positive integer")
    private Integer photoRefId; // FK to photo metadata or storage

    @Column(name = "captured_by")
    @Min(value = 1, message = "Captured by id must be a positive integer")
    private Integer capturedBy; // FK to user/device

    @Digits(integer = 9, fraction = 6)
    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Digits(integer = 9, fraction = 6)
    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Digits(integer = 5, fraction = 2)
    @Column(name = "accuracy", precision = 5, scale = 2)
    private BigDecimal accuracy;

    @Column(name = "capture_device", length = 50)
    @Size(max = 50, message = "Capture device must not exceed 50 characters")
    private String captureDevice;

    @Column(name = "location_description", length = 200)
    @Size(max = 200, message = "Location description must not exceed 200 characters")
    private String locationDescription;


    @Column(name = "file_path", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "File path must not be blank")
    private String filePath;

    @Column(name = "capture_time")
    @PastOrPresent(message = "Capture time timestamp must be in the past or present")
    private LocalDateTime captureTime;



    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    private PhotoStatus status;
}