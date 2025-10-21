package com.incede.nbfc.core.monolith.report.form60;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.incede.nbfc.core.monolith.exception.GlobalExceptionHandler;
import com.incede.nbfc.core.monolith.report.OutputFormat;
import com.incede.nbfc.core.monolith.report.ReportGenerator;
import com.incede.nbfc.core.monolith.report.ReportName;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {Form60ReportController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class Form60ReportControllerTest {
    @Autowired
    private Form60ReportController form60ReportController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @MockBean
    private ReportGenerator reportGenerator;

    /**
     * Test {@link Form60ReportController#preview(Form60ReportData)}.
     *
     * <p>Method under test: {@link Form60ReportController#preview(Form60ReportData)}
     */
    @Test
    @DisplayName("Test preview(Form60ReportData)")
    void testPreview() throws Exception {
        // Arrange
        when(reportGenerator.generate(
                Mockito.<ReportName>any(),
                Mockito.<Map<String, Object>>any(),
                Mockito.<Object>any(),
                Mockito.<OutputFormat>any()))
                .thenReturn("AXAXAXAX".getBytes("UTF-8"));

        Form60ReportData form60ReportData = new Form60ReportData();
        form60ReportData.setAddressLine("42 Main St");
        form60ReportData.setFullName("Dr Jane Doe");
        form60ReportData.setPanOrForm60Reason("Just cause");
        form60ReportData.setResident(true);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/v1/reports/form60/preview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                JsonMapper.builder()
                                        .findAndAddModules()
                                        .build()
                                        .writeValueAsString(form60ReportData));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(form60ReportController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"))
                .andExpect(content().string("AXAXAXAX"));
    }

    /**
     * Test {@link Form60ReportController#previewWithParams(Map)}.
     *
     * <p>Method under test: {@link Form60ReportController#previewWithParams(Map)}
     */
    @Test
    @DisplayName("Test previewWithParams(Map)")
    void testPreviewWithParams() throws Exception {
        // Arrange
        when(reportGenerator.generate(
                Mockito.<ReportName>any(),
                Mockito.<Map<String, Object>>any(),
                Mockito.<Object>any(),
                Mockito.<OutputFormat>any()))
                .thenReturn("AXAXAXAX".getBytes("UTF-8"));

        MockHttpServletRequestBuilder contentTypeResult =
                MockMvcRequestBuilders.post("/api/v1/reports/form60/preview/params")
                        .contentType(MediaType.APPLICATION_JSON);

        JsonMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();

        MockHttpServletRequestBuilder requestBuilder =
                contentTypeResult.content(jsonMapper.writeValueAsString(new HashMap<>()));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(form60ReportController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"))
                .andExpect(content().string("AXAXAXAX"));
    }
}
