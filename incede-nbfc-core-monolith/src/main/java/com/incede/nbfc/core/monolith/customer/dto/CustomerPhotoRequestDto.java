package com.incede.nbfc.core.monolith.customer.dto;

import com.incede.nbfc.core.monolith.customer.enums.PhotoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPhotoRequestDto {

    private String photoRefId;
    private Integer capturedBy;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal accuracy;
    private String captureDevice;
    private String locationDescription;
    private LocalDateTime captureTime;
    private PhotoStatus status;
    private String filePath;
    private Integer createdBy;
    private Integer updatedBy;

}
