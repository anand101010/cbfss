package com.incede.nbfc.core.monolith.report.form60;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class Form60ReportControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void previewParams_returnsPdf() throws Exception {
		String json = "{" +
			"\"firstName\":\"Ravi\"," +
			"\"middleName\":\"Kumar\"" +
			"}";

		byte[] bytes = mockMvc.perform(post("/api/v1/reports/form60/preview/params")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_PDF))
			.andReturn()
			.getResponse()
			.getContentAsByteArray();

		assertThat(bytes).isNotNull();
		assertThat(bytes.length).isGreaterThan(100); // basic sanity check
	}
} 