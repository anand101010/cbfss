package com.incede.nbfc.core.monolith.report;

import java.util.Map;

public interface ReportGenerator {
	byte[] generate(ReportName reportName, Map<String, Object> parameters, Object data, OutputFormat format);
} 