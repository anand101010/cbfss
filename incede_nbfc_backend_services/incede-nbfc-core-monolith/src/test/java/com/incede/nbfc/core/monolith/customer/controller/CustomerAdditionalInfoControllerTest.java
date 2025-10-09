package com.incede.nbfc.core.monolith.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.customer.service.CustomerAdditionalInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerAdditionalInfoControllerTest {

    @Mock
    private CustomerAdditionalInfoService additionalInfoService;

    @InjectMocks
    private CustomerAdditionalInfoController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID customerIdentity;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
        customerIdentity = UUID.randomUUID();
    }

    @Test
    void updateAdditionalInfo_validRequest_returnsCreated() throws Exception {
        CustomerAdditionalInfoRequestDto requestDto = createSampleRequestDto();
        CustomerAdditionalInfoResponseDto responseDto = createSampleResponseDto(requestDto);

        when(additionalInfoService.saveAdditionalInfo(eq(customerIdentity), any(CustomerAdditionalInfoRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/customers/" + customerIdentity + "/additional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identity").value(customerIdentity.toString()))
                .andExpect(jsonPath("$.customerCode").value("CUST123"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.additional.employment.occupationId").value(requestDto.getAdditional().getEmployment().getOccupationId().toString()));
    }

    @Test
    void getAdditionalInfo_validCustomerIdentity_returnsOk() throws Exception {
        CustomerAdditionalInfoRequestDto requestDto = createSampleRequestDto();
        CustomerAdditionalInfoResponseDto responseDto = createSampleResponseDto(requestDto);

        when(additionalInfoService.getAdditionalInfo(customerIdentity)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/customers/" + customerIdentity + "/additional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)) // Ensure JSON response is expected
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identity").value(customerIdentity.toString()))
                .andExpect(jsonPath("$.customerCode").value("CUST123"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.additional.employment.occupationId").value(responseDto.getAdditional().getEmployment().getOccupationId().toString()));
    }

    private CustomerAdditionalInfoRequestDto createSampleRequestDto() {
        CustomerEmploymentDto employment = CustomerEmploymentDto.builder()
                .occupationId(UUID.randomUUID())
                .designationId(UUID.randomUUID())
                .employer("Test Employer")
                .incomeSourceId(UUID.randomUUID())
                .monthlySalary(new BigDecimal("5000.00"))
                .annualIncome(new BigDecimal("60000.00"))
                .build();

        CustomerReferralDto referrals = CustomerReferralDto.builder()
                .referralSourceId(UUID.randomUUID())
                .canvassedTypeId(UUID.randomUUID())
                .canvasserStaffId(123)
                .build();

        CustomerProfileExtraDto profileExtra = CustomerProfileExtraDto.builder()
                .educationLevelId(UUID.randomUUID())
                .purposeId(UUID.randomUUID())
                .build();

        CustomerAssetDto assets = CustomerAssetDto.builder()
                .assetTypeId(UUID.randomUUID())
                .ownsAsset(true)
                .hasHomeLoan(false)
                .build();

        AdditionalInfoCustomerDto customer = AdditionalInfoCustomerDto.builder()
                .nationality(UUID.randomUUID())
                .preferredLanguageId(UUID.randomUUID())
                .residentialStatusId(UUID.randomUUID())
                .customerGroupId(UUID.randomUUID())
                .riskCategory(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .build();

        AdditionalInfoDto additionalInfo = AdditionalInfoDto.builder()
                .employment(employment)
                .referrals(referrals)
                .profileExtra(profileExtra)
                .customerAsset(assets)
                .customer(customer)
                .additionalReferenceValueDto(Collections.singletonList(AdditionalReferenceValueDto.builder().build()))
                .build();

        return CustomerAdditionalInfoRequestDto.builder()
                .additional(additionalInfo)
                .build();
    }

    private CustomerAdditionalInfoResponseDto createSampleResponseDto(CustomerAdditionalInfoRequestDto requestDto) {
        CustomerAdditionalInfoResponseDto.AdditionalInfoDto additionalInfo = CustomerAdditionalInfoResponseDto.AdditionalInfoDto.builder()
                .employment(requestDto.getAdditional().getEmployment())
                .referrals(requestDto.getAdditional().getReferrals())
                .profileExtra(requestDto.getAdditional().getProfileExtra())
                .assets(requestDto.getAdditional().getCustomerAsset())
                .additionalInfoCustomerDto(requestDto.getAdditional().getCustomer())
                .additionalReferenceValueDto(requestDto.getAdditional().getAdditionalReferenceValueDto())
                .build();

        return CustomerAdditionalInfoResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST123")
                .status("ACTIVE")
                .additional(additionalInfo)
                .build();
    }
}