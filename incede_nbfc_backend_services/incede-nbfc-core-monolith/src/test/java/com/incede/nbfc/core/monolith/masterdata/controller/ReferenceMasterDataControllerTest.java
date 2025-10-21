package com.incede.nbfc.core.monolith.masterdata.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.incede.nbfc.core.monolith.exception.GlobalExceptionHandler;
import com.incede.nbfc.core.monolith.masterdata.service.ReferenceMasterDataService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {ReferenceMasterDataController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ReferenceMasterDataControllerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private ReferenceMasterDataController referenceMasterDataController;

    @InjectMocks
    private ReferenceMasterDataController referenceMasterDataController2;

    @MockBean
    private ReferenceMasterDataService referenceMasterDataService;

    @Mock
    private ReferenceMasterDataService referenceMasterDataService2;

    /**
     * Test {@link ReferenceMasterDataController#getAllAddressTypes(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataController#getAllAddressTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllAddressTypes(UUID)")
    void testGetAllAddressTypes() throws Exception {
        // Arrange
        when(referenceMasterDataService.getAllAddressTypes(Mockito.<UUID>any()))
                .thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/v1/master/address-types");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(referenceMasterDataController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml;charset=UTF-8"))
                .andExpect(content().string("<List/>"));
    }

    /**
     * Test {@link ReferenceMasterDataController#getAllAddressProofTypes(UUID)}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataController#getAllAddressProofTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllAddressProofTypes(UUID); then status isNotFound()")
    void testGetAllAddressProofTypes_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(referenceMasterDataService.getAllAddressProofTypes(Mockito.<UUID>any()))
                .thenReturn(new ArrayList<>());

        FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();

        // Act and Assert
        MockMvcBuilders.standaloneSetup(referenceMasterDataController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    /**
     * Test {@link ReferenceMasterDataController#getAllAddressProofTypes(UUID)}.
     *
     * <ul>
     *   <li>When {@link MockMvcRequestBuilders#get(String, Object[])} {@code
     *       /api/v1/master/address-proof-type}.
     *   <li>Then status {@link StatusResultMatchers#isOk()}.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataController#getAllAddressProofTypes(UUID)}
     */
    @Test
    @DisplayName(
            "Test getAllAddressProofTypes(UUID); when get(String, Object[]) '/api/v1/master/address-proof-type'; then status isOk()")
    void testGetAllAddressProofTypes_whenGetApiV1MasterAddressProofType_thenStatusIsOk()
            throws Exception {
        // Arrange
        when(referenceMasterDataService.getAllAddressProofTypes(Mockito.<UUID>any()))
                .thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/v1/master/address-proof-type");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(referenceMasterDataController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml;charset=UTF-8"))
                .andExpect(content().string("<List/>"));
    }

    /**
     * Test {@link ReferenceMasterDataController#getAllResidentialStatuses(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataController#getAllResidentialStatuses(UUID)}
     */
    @Test
    @DisplayName("Test getAllResidentialStatuses(UUID)")
    void testGetAllResidentialStatuses() throws Exception {
        // Arrange
        when(referenceMasterDataService.getAllResidentialStatuses(Mockito.<UUID>any()))
                .thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/v1/master/residential-statuses");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(referenceMasterDataController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml;charset=UTF-8"))
                .andExpect(content().string("<List/>"));
    }

    /**
     * Test {@link ReferenceMasterDataController#getAllContactTypes(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataController#getAllContactTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllContactTypes(UUID)")
    void testGetAllContactTypes() throws Exception {
        // Arrange
        when(referenceMasterDataService.getAllContactTypes(Mockito.<UUID>any()))
                .thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/v1/master/contact-types");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(referenceMasterDataController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml;charset=UTF-8"))
                .andExpect(content().string("<List/>"));
    }

    /**
     * Test {@link ReferenceMasterDataController#getPincodeByNumber(Integer)}.
     *
     * <ul>
     *   <li>When one.
     *   <li>Then content string {@code <List/>}.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataController#getPincodeByNumber(Integer)}
     */
    @Test
    @DisplayName("Test getPincodeByNumber(Integer); when one; then content string '<List/>'")
    void testGetPincodeByNumber_whenOne_thenContentStringList() throws Exception {
        // Arrange
        when(referenceMasterDataService.getPincodeDetails(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/v1/master/pincodes/{pincode}", 1);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(referenceMasterDataController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml;charset=UTF-8"))
                .andExpect(content().string("<List/>"));
    }

    /**
     * Test {@link ReferenceMasterDataController#uploadExcel(MultipartFile, Integer)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataController#uploadExcel(MultipartFile, Integer)}
     */
    @Test
    @DisplayName("Test uploadExcel(MultipartFile, Integer)")
    @Disabled("TODO: Complete this test")
    void testUploadExcel() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Diffblue AI was unable to find a test

        // Arrange
        MockHttpServletRequestBuilder postResult =
                MockMvcRequestBuilders.post("/api/v1/master/pincodes/upload");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("Name", (InputStream) null);

        MockHttpServletRequestBuilder requestBuilder =
                postResult
                        .param("createdBy", String.valueOf(1))
                        .param("file", String.valueOf(mockMultipartFile));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(referenceMasterDataController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/xml;charset=UTF-8"))
                .andExpect(
                        content()
                                .string(
                                        "<ErrorResponse><timestamp>2025-10-15 21:02:09</timestamp><status>500</status><error>Internal Server"
                                                + " Error</error><message>An unexpected error occurred</message><path>/api/v1/master/pincodes/upload<"
                                                + "/path><errorCode>500</errorCode><correlationId>4a1d787a-84b4-4787-9d7e-50cc89270400</correlationId><"
                                                + "/ErrorResponse>"));
    }

    /**
     * Test {@link ReferenceMasterDataController#uploadExcel(MultipartFile, Integer)}.
     *
     * <ul>
     *   <li>Then StatusCode return {@link HttpStatus}.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataController#uploadExcel(MultipartFile, Integer)}
     */
    @Test
    @DisplayName("Test uploadExcel(MultipartFile, Integer); then StatusCode return HttpStatus")
    void testUploadExcel_thenStatusCodeReturnHttpStatus() throws IOException {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        ReferenceMasterDataService referenceMasterDataService = mock(ReferenceMasterDataService.class);
        doNothing()
                .when(referenceMasterDataService)
                .importFile(Mockito.<MultipartFile>any(), Mockito.<Integer>any());
        ReferenceMasterDataController referenceMasterDataController =
                new ReferenceMasterDataController(referenceMasterDataService);
        MockMultipartFile file =
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")));

        // Act
        ResponseEntity<String> actualUploadExcelResult =
                referenceMasterDataController.uploadExcel(file, 1);

        // Assert
        verify(referenceMasterDataService).importFile(isA(MultipartFile.class), eq(1));
        HttpStatusCode statusCode = actualUploadExcelResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("Data imported successfully!", actualUploadExcelResult.getBody());
        assertEquals(200, actualUploadExcelResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualUploadExcelResult.hasBody());
        assertTrue(actualUploadExcelResult.getHeaders().isEmpty());
    }
}
