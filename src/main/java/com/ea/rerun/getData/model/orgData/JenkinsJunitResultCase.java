package com.ea.rerun.getData.model.orgData;

/**
 * @author Jackey jaceky90.hj@gmail.com
 * @Date May 12, 2014
 * 
 * 		
 */
public class JenkinsJunitResultCase {
	private String packageName;
	private String className;
	private String testName;
	private JenkinsTestCaseStatusEnum caseStatus;
	private int failedSince;
	private String errorDetails;
	private String errorStackTrace;
	private String stdout;
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public JenkinsTestCaseStatusEnum getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(JenkinsTestCaseStatusEnum caseStatus) {
		this.caseStatus = caseStatus;
	}

	public int getFailedSince() {
		return failedSince;
	}

	public void setFailedSince(int failedSince) {
		this.failedSince = failedSince;
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
	
	
	@Override
	public String toString() {
		return "JenkinsJunitResultCase [packageName=" + packageName
				+ ", className=" + className + ", testName=" + testName
				+ ", caseStatus=" + caseStatus + ", failedSince=" + failedSince
				+ ", errorDetails=" + errorDetails + ", errorStackTrace="
				+ errorStackTrace + ", stdout=" + stdout + "]";
	}

}
