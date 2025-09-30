package com.incede.nbfc.core.monolith.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60RequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60ResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerForm60Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerForm60ControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerForm60Service form60Service;

    @InjectMocks
    private CustomerForm60Controller form60Controller;

    private ObjectMapper objectMapper;

    private UUID customerIdentity;
    private CustomerForm60RequestDto requestDto;
    private CustomerForm60ResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(form60Controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        customerIdentity = UUID.randomUUID();

        requestDto = new CustomerForm60RequestDto();
        requestDto.setCustomerId(1);
        requestDto.setBranchId(2);
        requestDto.setTransactionAmount(BigDecimal.valueOf(200000));
        requestDto.setTransactionDate(LocalDate.of(2025, 5, 25));
        requestDto.setModeOfTransaction("CASH");
        requestDto.setNumberOfPersons(1);
        requestDto.setAgriculturalIncome(BigDecimal.valueOf(150000));
        requestDto.setOtherIncome(BigDecimal.valueOf(50000));
        requestDto.setTaxableIncome(BigDecimal.valueOf(80000));
        requestDto.setNonTaxableIncome(BigDecimal.ZERO);
        requestDto.setPanCardApplicationDate(LocalDate.of(2025, 5, 20));
        requestDto.setPanCardApplicationAckNo("ACK123456");
        requestDto.setPidDocumentNo("PID123");
        requestDto.setPidIssuingAuthority("Govt");
        requestDto.setAddDocumentNo("ADDR123");
        requestDto.setAddIssuingAuthority("Govt");
        requestDto.setSubmissionDate(LocalDate.of(2025, 5, 26));
        requestDto.setFormFileId(123);
        requestDto.setCreatedBy(1);
        requestDto.setUpdatedBy(2);

        responseDto = new CustomerForm60ResponseDto();
        responseDto.setTransactionAmount(BigDecimal.valueOf(200000));
    }

    @Test
    void testSaveForm60_Success() throws Exception {
        when(form60Service.saveForm60(any(CustomerForm60RequestDto.class), eq(customerIdentity)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/customers/{customerIdentity}/form60", customerIdentity)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateForm60_Success() throws Exception {
        when(form60Service.updateForm60(eq(customerIdentity), eq(1), any(CustomerForm60RequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/customers/{customerIdentity}/form60/{form60Id}", customerIdentity, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

    }

    @Test
    void testGetForm60ById_Success() throws Exception {
        when(form60Service.getForm60ById(eq(customerIdentity), eq(1)))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Id}", customerIdentity, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
