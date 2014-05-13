package com.ea.rerun.getData.model.orgData;

import java.util.List;

import com.ibm.icu.math.BigDecimal;

public class JenkinsJunitResult {
	private int buildNumber;
	private BigDecimal totalDuration;
	private boolean keepLongStdio;
	private int totalCount;
	private int skipCount;
	private int failCount;

	List<JenkinsJunitResultSuite> suites;

	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

	public BigDecimal getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(BigDecimal totalDuration) {
		this.totalDuration = totalDuration;
	}

	public boolean isKeepLongStdio() {
		return keepLongStdio;
	}

	public void setKeepLongStdio(boolean keepLongStdio) {
		this.keepLongStdio = keepLongStdio;
	}

	public List<JenkinsJunitResultSuite> getSuites() {
		return suites;
	}

	public void setSuites(List<JenkinsJunitResultSuite> suites) {
		this.suites = suites;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getSkipCount() {
		return skipCount;
	}

	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

}
