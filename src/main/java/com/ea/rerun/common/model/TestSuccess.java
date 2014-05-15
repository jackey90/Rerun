package com.ea.rerun.common.model;

import com.ibm.icu.math.BigDecimal;

public class TestSuccess {
	private Test test;
	private String stdout;
	private int runNumber;
	private BigDecimal durationTime;

	public TestSuccess(Test test, String stdout, int runNumber,
			BigDecimal durationTime) {
		this.test = test;
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
