package com.incede.nbfc.core.monolith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.client.dto.BankValidationResponseDto;
import com.incede.nbfc.core.monolith.domain.dto.BankValidateRequestDto;
import com.incede.nbfc.core.monolith.exception.GlobalExceptionHandler;
import com.incede.nbfc.core.monolith.service.BankValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {BankValidationController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class BankValidationControllerTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private BankValidationController bankValidationController;

    @MockBean
    private BankValidationService bankValidationService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bankValidationController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    /**
     * Test {@link BankValidationController#validateBankAccount(BankValidateRequestDto)}.
     */
    @Test
    @DisplayName("Test validateBankAccount - success case")
    void testValidateBankAccount_success() throws Exception {
        // Arrange
        BankValidationResponseDto responseDto = new BankValidationResponseDto();
        responseDto.setStatus("SUCCESS");
        responseDto.setMessage("Bank account is valid");

        when(bankValidationService.validateBankAccount(any(BankValidateRequestDto.class)))
                .thenReturn(responseDto);

        BankValidateRequestDto requestDto = new BankValidateRequestDto();
        requestDto.setAccountNumber("1234567890");
        requestDto.setIfsc("SBIN0001234");

        String content = new ObjectMapper().writeValueAsString(requestDto);

        // Act & Assert
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/ext/bank/account/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bank account is valid"));

        // Verify service was called
        verify(bankValidationService).validateBankAccount(any(BankValidateRequestDto.class));
    }

    /**
     * Negative Test: Service throws RuntimeException
     */
    @Test
    @DisplayName("Test validateBankAccount - service throws exception -> 500")
    void testValidateBankAccount_failure() throws Exception {
        // Arrange
        when(bankValidationService.validateBankAccount(any(BankValidateRequestDto.class)))
                .thenThrow(new RuntimeException("Bank validation failed"));

        BankValidateRequestDto requestDto = new BankValidateRequestDto();
        requestDto.setAccountNumber("1234567890");
        requestDto.setIfsc("SBIN0001234");

        String content = new ObjectMapper().writeValueAsString(requestDto);

        // Act & Assert
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/ext/bank/account/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        ;
    }
}
