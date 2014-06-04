package com.ea.rerun.getData.model.orgData;

public class JenkinsModule {
	private String moduleName;
	private int nextBuildNumber;
	private JenkinsJunitResult lastBuildResult;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public int getNextBuildNumber() {
		return nextBuildNumber;
	}

	public void setNextBuildNumber(int nextBuildNumber) {
		this.nextBuildNumber = nextBuildNumber;
	}

	public JenkinsJunitResult getLastBuildResult() {
		return lastBuildResult;
	}

	public void setLastBuildResult(JenkinsJunitResult lastBuildResult) {
		this.lastBuildResult = lastBuildResult;
	}

	@Override
	public String toString() {
		return lastBuildResult.toString();
	}

}
