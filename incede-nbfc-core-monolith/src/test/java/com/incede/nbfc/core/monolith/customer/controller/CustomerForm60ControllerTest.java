package com.incede.nbfc.core.monolith.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60RequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60ResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.Form60UploadResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerForm60Service;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
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
    private UUID form60Identity;
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
        form60Identity = UUID.randomUUID();

        requestDto = new CustomerForm60RequestDto();
        requestDto.setCustomerId(UUID.randomUUID());
        requestDto.setBranchId(UUID.randomUUID());
        requestDto.setTransactionAmount(BigDecimal.valueOf(200000));
        requestDto.setTransactionDate(LocalDate.of(2025, 5, 25));
        requestDto.setModeOfTransaction("CASH");

        responseDto = new CustomerForm60ResponseDto();
        responseDto.setBranchId(10);
        responseDto.setTransactionAmount(BigDecimal.valueOf(200000));
    }

    // -------------------- SAVE -------------------- //

    @Test
    void testSaveForm60_Success() throws Exception {
        when(form60Service.saveForm60(any(CustomerForm60RequestDto.class), eq(customerIdentity)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/customers/{customerIdentity}/form60", customerIdentity)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.branchId").value(10));
    }

    @Test
    void testSaveForm60_ServiceThrowsException() throws Exception {
        when(form60Service.saveForm60(any(), eq(customerIdentity)))
                .thenThrow(new BusinessException("Error saving"));

        mockMvc.perform(post("/api/v1/customers/{customerIdentity}/form60", customerIdentity)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    // -------------------- UPDATE -------------------- //

    @Test
    void testUpdateForm60_Success() throws Exception {
        when(form60Service.updateForm60(eq(customerIdentity), eq(UUID.randomUUID()), any()))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/customers/{customerIdentity}/form60/{form60Id}", customerIdentity, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchId").value(10));
    }

    @Test
    void testUpdateForm60_ServiceThrowsException() throws Exception {
        when(form60Service.updateForm60(eq(customerIdentity), eq(UUID.randomUUID()), any()))
                .thenThrow(new ResourceNotFoundException("Form60 not found"));

        mockMvc.perform(put("/api/v1/customers/{customerIdentity}/form60/{form60Id}", customerIdentity, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    // -------------------- GET BY ID -------------------- //

    @Test
    void testGetForm60ById_Success() throws Exception {
        when(form60Service.getForm60ByIdentity(eq(customerIdentity), eq(UUID.randomUUID())))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Id}", customerIdentity, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchId").value(10));
    }

    @Test
    void testGetForm60ById_ServiceThrowsException() throws Exception {
        when(form60Service.getForm60ByIdentity(eq(customerIdentity), eq(UUID.randomUUID())))
                .thenThrow(new ResourceNotFoundException("Form60 not found"));

        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Id}", customerIdentity, 1))
                .andExpect(status().isInternalServerError());
    }

    // -------------------- PDF PREVIEW -------------------- //

    @Test
    void testGenerateForm60PreviewPdf_Success() throws Exception {
        when(form60Service.generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity)))
                .thenReturn(new byte[]{1, 2, 3});

        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/preview", customerIdentity, form60Identity))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    // -------------------- PDF DOWNLOAD -------------------- //

    @Test
    void testGenerateForm60DownloadPdf_Success() throws Exception {
        when(form60Service.generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity)))
                .thenReturn(new byte[]{1, 2, 3});

        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/download", customerIdentity, form60Identity))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    // -------------------- UPLOAD -------------------- //

    @Test
    void testUploadSignedForm60_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("signedForm60", "form60.pdf", "application/pdf", "data".getBytes());
        Form60UploadResponseDto uploadResponse = new Form60UploadResponseDto();
        uploadResponse.setForm60Identity(form60Identity);

        when(form60Service.uploadSignedForm60(eq(customerIdentity), eq(form60Identity), any()))
                .thenReturn(uploadResponse);

        mockMvc.perform(multipart("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/upload", customerIdentity, form60Identity)
                        .file(file))
                .andExpect(status().isOk());
    }

    @Test
    void testUploadSignedForm60_EmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("signedForm60", "form60.pdf", "application/pdf", new byte[0]);

        when(form60Service.uploadSignedForm60(eq(customerIdentity), eq(form60Identity), any()))
                .thenThrow(new BusinessException("File is empty"));

        mockMvc.perform(multipart("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/upload", customerIdentity, form60Identity)
                        .file(file))
                .andExpect(status().isInternalServerError());
    }

}
