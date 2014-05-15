package com.ea.rerun.analyse.model;

import com.ea.rerun.common.model.TestCase;

public class MavenRerunTestCase {
	private String viewName;
	private String jobName;
	private TestCase testCase;

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public TestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}

}
