package com.ea.rerun.getData.model.orgData;

import java.util.List;

public class JenkinsJob {
	private String jobName;
	private List<JenkinsModule> modules;
	private String pomPath;
	private String goals;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public List<JenkinsModule> getModules() {
		return modules;
	}

	public void setModules(List<JenkinsModule> modules) {
		this.modules = modules;
	}

	public String getPomPath() {
		return pomPath;
	}

	public void setPomPath(String pomPath) {
		this.pomPath = pomPath;
	}

	public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	private String getModulesString() {
		StringBuffer sb = new StringBuffer();
		String moduleStr = "";
		for (JenkinsModule module : modules) {
			moduleStr = module.toString();
			if (!moduleStr.equals("")) {
				sb.append(moduleStr);
			}
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		String modulesStr = getModulesString();
		if (!modulesStr.equals("")) {
			return jobName + "\n    " + modulesStr;
		} else {
			return "";
		}
	}

}
