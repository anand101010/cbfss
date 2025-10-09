package com.incede.nbfc.core.monolith.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *Dto which accepts from our application client
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankValidateRequestDto
{
    @ToString.Exclude
    private String accountNumber;
    @ToString.Exclude
    private String ifsc;
}
