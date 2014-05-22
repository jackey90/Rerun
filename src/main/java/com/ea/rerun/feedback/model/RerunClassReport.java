package com.ea.rerun.feedback.model;

import java.util.List;
import java.util.Map;

import com.ea.rerun.common.model.Bug;
import com.ea.rerun.common.model.TestCase;

public class RerunClassReport {
	private String className;
	private int failNumber;
	private List<TestCase> allCases;
	private Map<String, List<TestCase>> failureList;
	private List<Bug> bugs;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getFailNumber() {
		return failNumber;
	}

	public void setFailNumber(int failNumber) {
		this.failNumber = failNumber;
	}

	public List<TestCase> getAllCases() {
		return allCases;
	}

	public void setAllCases(List<TestCase> allCases) {
		this.allCases = allCases;
	}

	public Map<String, List<TestCase>> getFailureList() {
		return failureList;
	}

	public void setFailureList(Map<String, List<TestCase>> failureList) {
		this.failureList = failureList;
	}

	public List<Bug> getBugs() {
		return bugs;
	}

	public void setBugs(List<Bug> bugs) {
		this.bugs = bugs;
	}

}
