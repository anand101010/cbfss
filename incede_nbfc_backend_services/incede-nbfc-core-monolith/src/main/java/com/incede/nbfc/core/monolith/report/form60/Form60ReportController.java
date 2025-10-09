package com.incede.nbfc.core.monolith.report.form60;

import com.incede.nbfc.core.monolith.report.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports/form60")
@RequiredArgsConstructor
public class Form60ReportController {
	private final ReportGenerator reportGenerator;

	@PostMapping("/preview")
	public ResponseEntity<byte[]> preview(@RequestBody Form60ReportData data) {
		Map<String, Object> params = new HashMap<>();
		params.put("generatedBy", "NBFC Portal");
		byte[] pdf = reportGenerator.generate(ReportName.FORM60, params, data, OutputFormat.PDF);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.inline().filename("form60-preview.pdf").build());
		return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
	}

	@PostMapping("/preview/params")
	public ResponseEntity<byte[]> previewWithParams(@RequestBody Map<String, Object> parameters) {
		byte[] pdf = reportGenerator.generate(ReportName.FORM60, parameters, null, OutputFormat.PDF);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.inline().filename("form60-preview.pdf").build());
		return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
	}
} 