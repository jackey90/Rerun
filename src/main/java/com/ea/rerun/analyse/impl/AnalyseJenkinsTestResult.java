package com.ea.rerun.analyse.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ea.rerun.analyse.IAnalyse;
import com.ea.rerun.analyse.model.MavenRerunTestCase;
import com.ea.rerun.common.model.TestCase;
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

	@Override
	public List<MavenRerunTestCase> getAnalyseData() {
		if (jenkinsResult != null) {
			List<MavenRerunTestCase> list = new ArrayList<MavenRerunTestCase>();
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
								//String moduleName = module.getModuleName();
								JenkinsJunitResult testResult = module
										.getLastBuildResult();
								for (JenkinsJunitResultSuite jenkinsSuite : testResult
										.getSuites()) {
									for (JenkinsJunitResultCase jenkinsCase : jenkinsSuite
											.getCases()) {
										MavenRerunTestCase command = new MavenRerunTestCase();
										command.setViewName(viewName);
										command.setJobName(jobName);
										TestCase testCase = new TestCase();
										if (pomPath != null) {
											String[] pathArray = pomPath
													.split("\\\\\\\\");
											if (pathArray.length >= 3) {
												if (!pathArray[pathArray.length - 3]
														.contains("nexus")) {
													testCase.setBranch(pathArray[pathArray.length - 3]);
													testCase.setBundle(pathArray[pathArray.length - 2]);
												} else {
													testCase.setBranch(pathArray[pathArray.length - 4]);
													testCase.setBundle(pathArray[pathArray.length - 3]
															+ "\\"
															+ pathArray[pathArray.length - 2]);
												}
											}
										}
										testCase.setPack(jenkinsCase
												.getPackageName());
										testCase.setClassName(jenkinsCase
												.getClassName());
										testCase.setTestName(jenkinsCase
												.getTestName());
										testCase.setPomPath(pomPath);
										testCase.setGoals(goals);
										command.setTestCase(testCase);
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
