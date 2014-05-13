package com.ea.rerun.analyse.model;

public class MavenCommand {
	private String viewName;
	private String jobName;
	private String moduleName;
	private String casePackage;
	private String caseClass;
	private String testName;
	private String command;
	private String goals;

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

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getCasePackage() {
		return casePackage;
	}

	public void setCasePackage(String casePackage) {
		this.casePackage = casePackage;
	}

	public String getCaseClass() {
		return caseClass;
	}

	public void setCaseClass(String caseClass) {
		this.caseClass = caseClass;
	}

	

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
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

}
