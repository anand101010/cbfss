package com.incede.nbfc.core.monolith.report.form60;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Form60ReportData {
	private String fullName;
	private String panOrForm60Reason;
	private String addressLine;
	private Boolean resident;

	public String getFullName() { return fullName; }
	public void setFullName(String fullName) { this.fullName = fullName; }
	public String getPanOrForm60Reason() { return panOrForm60Reason; }
	public void setPanOrForm60Reason(String panOrForm60Reason) { this.panOrForm60Reason = panOrForm60Reason; }
	public String getAddressLine() { return addressLine; }
	public void setAddressLine(String addressLine) { this.addressLine = addressLine; }
	public Boolean getResident() { return resident; }
	public void setResident(Boolean resident) { this.resident = resident; }
} 