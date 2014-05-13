package com.ea.rerun.getData.model.orgData;

import java.util.List;

import com.ibm.icu.math.BigDecimal;

public class JenkinsJunitResultSuite {
	private String file;
	private String suiteName;
	private BigDecimal suiteDuration;
	private List<JenkinsJunitResultCase> cases;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public BigDecimal getSuiteDuration() {
		return suiteDuration;
	}

	public void setSuiteDuration(BigDecimal suiteDuration) {
		this.suiteDuration = suiteDuration;
	}

	public List<JenkinsJunitResultCase> getCases() {
		return cases;
	}

	public void setCases(List<JenkinsJunitResultCase> cases) {
		this.cases = cases;
	}

}
