package com.ea.rerun.analyse.model;

import java.util.List;


public class MavenRerunResult {
	private int totalCount;
	private int skipCount;
	private int failCount;
	private String bundle;
	private RerunResultStatusEnun rerunStatus;
	private List<MavenRerunResultCase> rerunCases;

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

	public String getBundle() {
		return bundle;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public RerunResultStatusEnun getRerunStatus() {
		return rerunStatus;
	}

	public void setRerunStatus(RerunResultStatusEnun rerunStatus) {
		this.rerunStatus = rerunStatus;
	}

	public List<MavenRerunResultCase> getRerunCases() {
		return rerunCases;
	}

	public void setRerunCases(List<MavenRerunResultCase> rerunCases) {
		this.rerunCases = rerunCases;
	}

}
