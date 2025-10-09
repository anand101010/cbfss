package com.incede.nbfc.core.monolith.enums;
import lombok.Getter;

@Getter
public enum KycType {
    VOTER_ID(1),
    PAN(2),
    DRIVING_LICENSE(3);

    private final int kycId;

    KycType(int kycId) {
        this.kycId = kycId;
    }

    // Static method to get enum from id
    public static KycType getKycTypeFromIdType(int kycId) {
        for (KycType type : KycType.values()) {
            if (type.getKycId() == kycId) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid KycType id: " + kycId);
    }
}
