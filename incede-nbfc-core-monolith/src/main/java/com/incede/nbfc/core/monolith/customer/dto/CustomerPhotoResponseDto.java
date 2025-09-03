package com.incede.nbfc.core.monolith.customer.dto;

import com.incede.nbfc.core.monolith.customer.enums.PhotoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPhotoResponseDto {
    private UUID identity;
    private String customerCode;
    private String status;
    private List<PhotoDetail> photo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PhotoDetail{
        private Integer photoId;
        private Integer photoRefId;
        private LocalDateTime captureTime;
        private PhotoStatus status;
    }
}
