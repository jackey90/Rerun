package com.ea.rerun.common.model;

import com.ibm.icu.math.BigDecimal;

public class TestFailure {
	private Test test;
	private String errorDetails;
	private String errorStackTrace;
	private String stdout;
	private int runNumber;
	private BigDecimal durationTime;

	public TestFailure(Test test, String errorDetails, String errorStackTrace,
			String stdout, int runNumber, BigDecimal durationTime) {
		this.test = test;
		this.errorDetails = errorDetails;
		this.errorStackTrace = errorStackTrace;
		this.stdout = stdout;
		this.runNumber = runNumber;
		this.durationTime = durationTime;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getErrorStackTrace() {
		return errorStackTrace;
	}

	public void setErrorStackTrace(String errorStackTrace) {
		this.errorStackTrace = errorStackTrace;
	}

	public String getStdout() {
		return stdout;
	}

	public void setStdout(String stdout) {
		this.stdout = stdout;
	}

	public int getRunNumber() {
		return runNumber;
	}

	public void setRunNumber(int runNumber) {
		this.runNumber = runNumber;
	}

	public BigDecimal getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(BigDecimal durationTime) {
		this.durationTime = durationTime;
	}

}
