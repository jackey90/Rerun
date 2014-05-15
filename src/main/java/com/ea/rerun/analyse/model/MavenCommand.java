package com.ea.rerun.analyse.model;

import com.ea.rerun.common.model.TestCase;

public class MavenCommand {
	private String viewName;
	private String jobName;
	private TestCase testCase;
	private String command;
	private String goals;
	private String pomPath;

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

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	public String getPomPath() {
		return pomPath;
	}

	public void setPomPath(String pomPath) {
		this.pomPath = pomPath;
	}

}
