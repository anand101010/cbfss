
package com.incede.nbfc.core.monolith.customer.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60RequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60ResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.Form60UploadResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerForm60Service;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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
    private UUID branchId;
    private CustomerForm60RequestDto requestDto;
    private CustomerForm60ResponseDto responseDto;
    @ControllerAdvice
    public static class ExceptionControllerAdvice {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<String> handleBusinessException(BusinessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper);
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        org.springframework.http.converter.ByteArrayHttpMessageConverter byteConverter = new org.springframework.http.converter.ByteArrayHttpMessageConverter();
        mockMvc = MockMvcBuilders.standaloneSetup(form60Controller)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .setMessageConverters(jsonConverter, stringConverter, byteConverter)
                .build();
        customerIdentity = UUID.randomUUID();
        form60Identity = UUID.randomUUID();
        branchId = UUID.randomUUID();
        requestDto = new CustomerForm60RequestDto();
        requestDto.setCustomerId(customerIdentity);
        requestDto.setBranchId(branchId);
        requestDto.setTransactionAmount(BigDecimal.valueOf(200000));
        requestDto.setTransactionDate(LocalDate.of(2025, 5, 25));
        requestDto.setModeOfTransaction("CASH");
        requestDto.setCreatedBy(1);
        requestDto.setFormFileId(1);
        requestDto.setDocRefId("DOC123");
        requestDto.setFilePath("/path/to/file");
        responseDto = CustomerForm60ResponseDto.builder()
                .identity(form60Identity)
                .branchId(1)
                .transactionAmount(BigDecimal.valueOf(200000))
                .transactionDate(LocalDate.of(2025, 5, 25))
                .modeOfTransaction("CASH")
                .formFileId(1)
                .docRefId("DOC123")
                .filePath("/path/to/file")
                .build();
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionAmount").value(200000));
        verify(form60Service, times(1)).saveForm60(any(CustomerForm60RequestDto.class), eq(customerIdentity));
    }
    @Test
    void testSaveForm60_ServiceThrowsException() throws Exception {
        when(form60Service.saveForm60(any(), eq(customerIdentity)))
                .thenThrow(new BusinessException("Error saving", ErrorCodes.VALIDATION_FAILED));
        mockMvc.perform(post("/api/v1/customers/{customerIdentity}/form60", customerIdentity)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
        verify(form60Service, times(1)).saveForm60(any(), eq(customerIdentity));
    }
    // -------------------- UPDATE -------------------- //
    @Test
    void testUpdateForm60_Success() throws Exception {
        when(form60Service.updateForm60(eq(customerIdentity), eq(form60Identity), any()))
                .thenReturn(responseDto);
        mockMvc.perform(put("/api/v1/customers/{customerIdentity}/form60/{form60Identity}",
                        customerIdentity, form60Identity)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionAmount").value(200000));
        verify(form60Service, times(1)).updateForm60(eq(customerIdentity), eq(form60Identity), any());
    }
    @Test
    void testUpdateForm60_ServiceThrowsException() throws Exception {
        when(form60Service.updateForm60(eq(customerIdentity), eq(form60Identity), any()))
                .thenThrow(new BusinessException("Form60 not found", ErrorCodes.NOT_FOUND));
        mockMvc.perform(put("/api/v1/customers/{customerIdentity}/form60/{form60Identity}",
                        customerIdentity, form60Identity)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
        verify(form60Service, times(1)).updateForm60(eq(customerIdentity), eq(form60Identity), any());
    }
    // -------------------- GET BY ID -------------------- //
    @Test
    void testGetForm60ByIdentity_Success() throws Exception {
        when(form60Service.getForm60ByIdentity(eq(customerIdentity), eq(form60Identity)))
                .thenReturn(responseDto);
        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}",
                        customerIdentity, form60Identity))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionAmount").value(200000));
        verify(form60Service, times(1)).getForm60ByIdentity(eq(customerIdentity), eq(form60Identity));
    }
    @Test
    void testGetForm60ByIdentity_ServiceThrowsException() throws Exception {
        when(form60Service.getForm60ByIdentity(eq(customerIdentity), eq(form60Identity)))
                .thenThrow(new BusinessException("Form60 not found", ErrorCodes.NOT_FOUND));
        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}",
                        customerIdentity, form60Identity))
                .andExpect(status().isBadRequest());
        verify(form60Service, times(1)).getForm60ByIdentity(eq(customerIdentity), eq(form60Identity));
    }
    @Test
    void testGetForm60ByIdentity_CustomerNotFound() throws Exception {
        when(form60Service.getForm60ByIdentity(eq(customerIdentity), eq(form60Identity)))
                .thenThrow(new ResourceNotFoundException("Customer", customerIdentity.toString()));
        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}",
                        customerIdentity, form60Identity))
                .andExpect(status().isNotFound());
        verify(form60Service, times(1)).getForm60ByIdentity(eq(customerIdentity), eq(form60Identity));
    }
    // -------------------- PDF PREVIEW -------------------- //
    @Test
    void testGenerateForm60PreviewPdf_Success() throws Exception {
        byte[] pdfBytes = new byte[]{1, 2, 3};
        when(form60Service.generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity)))
                .thenReturn(pdfBytes);
        byte[] result = mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/preview",
                        customerIdentity, form60Identity))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"form60-preview.pdf\""))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        assertArrayEquals(pdfBytes, result);
        verify(form60Service, times(1)).generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity));
    }
    @Test
    void testGenerateForm60PreviewPdf_WithCustomFilename() throws Exception {
        byte[] pdfBytes = new byte[]{1, 2, 3};
        when(form60Service.generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity)))
                .thenReturn(pdfBytes);
        byte[] result = mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/preview",
                        customerIdentity, form60Identity)
                        .param("filename", "custom-form60.pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"custom-form60.pdf\""))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        assertArrayEquals(pdfBytes, result);
        verify(form60Service, times(1)).generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity));
    }
    @Test
    void testGenerateForm60PreviewPdf_ResourceNotFound() throws Exception {
        when(form60Service.generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity)))
                .thenThrow(new ResourceNotFoundException("Form60", form60Identity.toString()));
        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/preview",
                        customerIdentity, form60Identity))
                .andExpect(status().isNotFound());
        verify(form60Service, times(1)).generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity));
    }
    @Test
    void testGenerateForm60PreviewPdf_BusinessException() throws Exception {
        when(form60Service.generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity)))
                .thenThrow(new BusinessException("Unable to generate Form60 report", ErrorCodes.INTERNAL_SERVER_ERROR));
        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/preview",
                        customerIdentity, form60Identity))
                .andExpect(status().isBadRequest());
        verify(form60Service, times(1)).generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity));
    }
    // -------------------- PDF DOWNLOAD -------------------- //
    @Test
    void testGenerateForm60DownloadPdf_Success() throws Exception {
        byte[] pdfBytes = new byte[]{1, 2, 3};
        when(form60Service.generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity)))
                .thenReturn(pdfBytes);
        byte[] result = mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/download",
                        customerIdentity, form60Identity))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"form60-preview.pdf\""))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        assertArrayEquals(pdfBytes, result);
        verify(form60Service, times(1)).generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity));
    }
    @Test
    void testGenerateForm60DownloadPdf_WithCustomFilename() throws Exception {
        byte[] pdfBytes = new byte[]{1, 2, 3};
        when(form60Service.generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity)))
                .thenReturn(pdfBytes);
        byte[] result = mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/download",
                        customerIdentity, form60Identity)
                        .param("filename", "download-form60.pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"download-form60.pdf\""))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        assertArrayEquals(pdfBytes, result);
        verify(form60Service, times(1)).generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity));
    }
    @Test
    void testGenerateForm60DownloadPdf_ResourceNotFound() throws Exception {
        when(form60Service.generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity)))
                .thenThrow(new ResourceNotFoundException("Form60", form60Identity.toString()));
        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/download",
                        customerIdentity, form60Identity))
                .andExpect(status().isNotFound());
        verify(form60Service, times(1)).generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity));
    }
    @Test
    void testGenerateForm60DownloadPdf_BusinessException() throws Exception {
        when(form60Service.generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity)))
                .thenThrow(new BusinessException("Unable to generate Form60 report", ErrorCodes.INTERNAL_SERVER_ERROR));
        mockMvc.perform(get("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/download",
                        customerIdentity, form60Identity))
                .andExpect(status().isBadRequest());
        verify(form60Service, times(1)).generateForm60PreviewPdf(eq(customerIdentity), eq(form60Identity));
    }
    // -------------------- UPLOAD -------------------- //
    @Test
    void testUploadSignedForm60_Success() throws Exception {
        Form60UploadResponseDto uploadResponse = Form60UploadResponseDto.builder()
                .form60Identity(form60Identity)
                .pdfDocRefId("DOC123")
                .filePath("/path/to/file")
                .uploadedAt(OffsetDateTime.now())
                .build();
        when(form60Service.uploadSignedForm60(eq(customerIdentity), eq(form60Identity)))
                .thenReturn(uploadResponse);
        mockMvc.perform(post("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/upload",
                        customerIdentity, form60Identity))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pdfDocRefId").value("DOC123"))
                .andExpect(jsonPath("$.filePath").value("/path/to/file"));
        verify(form60Service, times(1)).uploadSignedForm60(eq(customerIdentity), eq(form60Identity));
    }
    @Test
    void testUploadSignedForm60_Form60NotFound() throws Exception {
        when(form60Service.uploadSignedForm60(eq(customerIdentity), eq(form60Identity)))
                .thenThrow(new ResourceNotFoundException("Form60", form60Identity.toString()));
        mockMvc.perform(post("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/upload",
                        customerIdentity, form60Identity))
                .andExpect(status().isNotFound());
        verify(form60Service, times(1)).uploadSignedForm60(eq(customerIdentity), eq(form60Identity));
    }
    @Test
    void testUploadSignedForm60_CustomerNotFound() throws Exception {
        when(form60Service.uploadSignedForm60(eq(customerIdentity), eq(form60Identity)))
                .thenThrow(new ResourceNotFoundException("Customer", customerIdentity.toString()));
        mockMvc.perform(post("/api/v1/customers/{customerIdentity}/form60/{form60Identity}/upload",
                        customerIdentity, form60Identity))
                .andExpect(status().isNotFound());
        verify(form60Service, times(1)).uploadSignedForm60(eq(customerIdentity), eq(form60Identity));
    }
}
 
 