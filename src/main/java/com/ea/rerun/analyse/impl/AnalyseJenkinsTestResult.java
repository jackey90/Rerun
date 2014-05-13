package com.ea.rerun.analyse.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ea.rerun.analyse.IAnalyse;
import com.ea.rerun.analyse.model.MavenCommand;
import com.ea.rerun.getData.model.orgData.JenkinsJob;
import com.ea.rerun.getData.model.orgData.JenkinsJunitResult;
import com.ea.rerun.getData.model.orgData.JenkinsJunitResultCase;
import com.ea.rerun.getData.model.orgData.JenkinsJunitResultSuite;
import com.ea.rerun.getData.model.orgData.JenkinsModule;
import com.ea.rerun.getData.model.orgData.JenkinsTestResult;

/**
 * @author Jackey jaceky90.hj@gmail.com
 * @Date May 8, 2014
 * 
 *       analyse the jenkins test result, and generate the maven commands to
 *       rerun the failures.
 */
public class AnalyseJenkinsTestResult implements IAnalyse {
	public JenkinsTestResult jenkinsResult;

	public AnalyseJenkinsTestResult(JenkinsTestResult jenkinsResult) {
		this.jenkinsResult = jenkinsResult;
	}

	public List<MavenCommand> jenkinsResult2MvnCmd() {
		if (jenkinsResult != null) {
			List<MavenCommand> list = new ArrayList<MavenCommand>();
			for (Map.Entry<String, List<JenkinsJob>> views : jenkinsResult
					.getViews().entrySet()) {
				String viewName = views.getKey();
				List<JenkinsJob> jobList = views.getValue();
				if (jobList != null) {
					for (JenkinsJob job : jobList) {
						String jobName = job.getJobName();
						String pomPath = job.getPomPath();
						String goals = job.getGoals();
						List<JenkinsModule> moduleList = job.getModules();
						if (moduleList != null) {
							for (JenkinsModule module : moduleList) {
								String moduleName = module.getModuleName();
								JenkinsJunitResult testResult = module
										.getLastBuildResult();
								for (JenkinsJunitResultSuite jenkinsSuite : testResult
										.getSuites()) {
									for (JenkinsJunitResultCase jenkinsCase : jenkinsSuite
											.getCases()) {
										MavenCommand command = new MavenCommand();
										command.setViewName(viewName);
										command.setJobName(jobName);
										command.setModuleName(moduleName);
										command.setCasePackage(jenkinsCase
												.getPackageName());
										command.setCaseClass(jenkinsCase
												.getClassName());
										command.setTestName(jenkinsCase
												.getTestName());
										command.setGoals(goals);
										command.setCommand("mvn -B -F "
												+ pomPath
												+ " "
												+ goals
												+ " -Dtest="
												+ jenkinsCase.getPackageName()
												+ "."
												+ jenkinsCase.getClassName()
												+ "#"
												+ jenkinsCase.getTestName());
										list.add(command);
									}
								}
							}
						}
					}
				}
			}
			
			return list;
		}
		return null;
	}
}
