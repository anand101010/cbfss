package com.incede.nbfc.core.monolith.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JasperReportGenerator implements ReportGenerator {
	private final Map<String, JasperReport> compiledCache = new ConcurrentHashMap<>();

	@Override
	public byte[] generate(ReportName reportName, Map<String, Object> parameters, Object data, OutputFormat format) {
		JasperReport report = compiledCache.computeIfAbsent(reportName.name(), key -> compile(reportName));
		JRDataSource dataSource = toDataSource(data);
		try {
			JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
			if (format == OutputFormat.PDF) {
				return JasperExportManager.exportReportToPdf(print);
			}
			if (format == OutputFormat.HTML) {
				JasperExportManager.exportReportToXml(print); // placeholder; not returning HTML bytes here
				return new byte[0];
			}
			throw new IllegalArgumentException("Unsupported format: " + format);
		} catch (JRException e) {
			throw new IllegalStateException("Failed to generate report: " + reportName, e);
		}
	}

	private JasperReport compile(ReportName reportName) {
		String path = "/reports/" + reportName.name().toLowerCase() + ".jrxml";
		try (InputStream in = getClass().getResourceAsStream(path)) {
			if (in == null) throw new IllegalStateException("JRXML not found: " + path);
			return JasperCompileManager.compileReport(in);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to compile JRXML: " + path, e);
		}
	}

	private JRDataSource toDataSource(Object data) {
		if (data == null) return new JREmptyDataSource();
		if (data instanceof JRDataSource) return (JRDataSource) data;
		if (data instanceof Collection<?>) return new JRBeanCollectionDataSource((Collection<?>) data);
		if (data instanceof Iterable<?>) {
			ArrayList<Object> list = new ArrayList<>();
			for (Object item : (Iterable<?>) data) list.add(item);
			return new JRBeanCollectionDataSource(list);
		}
		return new JRBeanCollectionDataSource(Collections.singletonList(data));
	}
} 