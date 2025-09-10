package com.incede.nbfc.core.monolith.controller.IntegrationTest;


import com.incede.nbfc.core.monolith.enums.KycType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class KycIntegrationTest
{

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGenerateOtpIntegration() throws Exception {
        String requestJson = """
            {
              "aadhaarNumber": "123412341234"
            }
        """;

        mockMvc.perform(post("/ext/ekyc/otp/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                ;
    }

    @Test
    void testValidateKycWithoutDobForDL_ShouldFail() throws Exception {
        mockMvc.perform(get("/validate/kyc")
                        .param("idNumber", "DL12345")
                        .param("kycId", String.valueOf(KycType.DRIVING_LICENSE.getKycId())))
                .andExpect(status().isBadRequest());  // because dob missing
    }

    @Test
    void testValidateKycWithDobForDL_ShouldPass() throws Exception {
        mockMvc.perform(get("/validate/kyc")
                        .param("idNumber", "DL12345")
                        .param("kycId", String.valueOf(KycType.DRIVING_LICENSE.getKycId()))
                        .param("dob", "1990-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));  // assuming your response has status
    }

    @Test
    void testValidateKycForPanWithoutDob_ShouldPass() throws Exception {
        mockMvc.perform(get("/validate/kyc")
                        .param("idNumber", "ABCDE1234F")
                        .param("kycId", String.valueOf(KycType.PAN.getKycId())))
                .andExpect(status().isOk());
    }
}
