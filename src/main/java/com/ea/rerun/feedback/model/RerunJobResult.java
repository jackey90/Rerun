package com.ea.rerun.feedback.model;

import java.util.ArrayList;
import java.util.List;

import com.ea.rerun.common.model.TestCase;

public class RerunJobResult {
	private String viewName;
	private String jobName;

	private List<TestCase> success;
	private List<TestCase> failures;
	private List<TestCase> errors;
	private List<TestCase> skips;

	public RerunJobResult(String viewName, String jobName) {
		this.viewName = viewName;
		this.jobName = jobName;
		success = new ArrayList<TestCase>();
		failures = new ArrayList<TestCase>();
		errors = new ArrayList<TestCase>();
		skips = new ArrayList<TestCase>();
	}

	public void addTestCase(TestCase testCase) {
		switch (testCase.getResult().getResultType()) {
		case Error:
			errors.add(testCase);
			break;
		case Successed:
			success.add(testCase);
			break;
		case Skiped:
			skips.add(testCase);
			break;
		default:
			failures.add(testCase);
			break;
		}

	}

	public int getSuccessCount() {
		return success.size();
	}

	public int getFailureCount() {
		return failures.size();
	}

	public int getErrorCount() {
		return errors.size();
	}

	public int getSkipCount() {
		return skips.size();
	}

	public int getAllCount() {
		return getSuccessCount() + getFailureCount() + getErrorCount()
				+ getSkipCount();
	}

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

	public List<TestCase> getSuccess() {
		return success;
	}

	public void setSuccess(List<TestCase> success) {
		this.success = success;
	}

	public List<TestCase> getFailures() {
		return failures;
	}

	public void setFailures(List<TestCase> failures) {
		this.failures = failures;
	}

	public List<TestCase> getErrors() {
		return errors;
	}

	public void setErrors(List<TestCase> errors) {
		this.errors = errors;
	}

	public List<TestCase> getSkips() {
		return skips;
	}

	public void setSkips(List<TestCase> skips) {
		this.skips = skips;
	}

}
