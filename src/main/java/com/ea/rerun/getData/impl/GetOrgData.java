package com.ea.rerun.getData.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Node;

import com.ea.rerun.common.util.FileAnalyser;
import com.ea.rerun.common.util.PrintUtil;
import com.ea.rerun.common.util.StringUtil;
import com.ea.rerun.common.util.XMLAnalyser;
import com.ea.rerun.getData.IGetData;
import com.ea.rerun.getData.model.config.JenkinsRootConfig;
import com.ea.rerun.getData.model.config.RerunConfig;
import com.ea.rerun.getData.model.orgData.JenkinsJob;
import com.ea.rerun.getData.model.orgData.JenkinsJunitResult;
import com.ea.rerun.getData.model.orgData.JenkinsJunitResultCase;
import com.ea.rerun.getData.model.orgData.JenkinsJunitResultSuite;
import com.ea.rerun.getData.model.orgData.JenkinsModule;
import com.ea.rerun.getData.model.orgData.JenkinsTestCaseStatusEnum;
import com.ea.rerun.getData.model.orgData.JenkinsTestResult;
import com.ibm.icu.math.BigDecimal;

/**
 * @author Jackey
 * @Date May 8, 2014
 * 
 *       get JenkinsTestResult according to the RerunConfig
 */
public class GetOrgData implements IGetData {
	private RerunConfig rerunConfig;
	private JenkinsRootConfig jenkinsConfig;
	private String jenKinsFolder;

	public JenkinsTestResult getData() {
		initConfig();
		return getFailedJenkinsTestResult();
	}

	public void initConfig() {
		rerunConfig = RerunConfig.getInstance();
		jenKinsFolder = rerunConfig.getJenkinsConfig().getJenkinsFolder();
		jenkinsConfig = JenkinsRootConfig.getInstance(jenKinsFolder
				+ "\\config.xml");
	}

	public JenkinsTestResult getFailedJenkinsTestResult() {
		if (rerunConfig != null && jenkinsConfig != null) {
			JenkinsTestResult result = new JenkinsTestResult();
			result.setViews(getFailedJenkinsTestViews());
			return result;
		}

		return null;
	}

	public Map<String, List<JenkinsJob>> getFailedJenkinsTestViews() {
		Map<String, List<JenkinsJob>> map = new HashMap<String, List<JenkinsJob>>();
		Map<String, List<String>> rerunViews = rerunConfig.getJenkinsConfig()
				.getViews();
		Map<String, List<String>> jenKinsViews = jenkinsConfig.getViews();
		if (jenKinsViews == null || jenKinsViews.isEmpty()) {
			PrintUtil.error("The jenkins views is empty!");
			return null;
		} else {
			if (rerunViews == null || rerunViews.isEmpty()) {
				for (Map.Entry<String, List<String>> jenkinsViewEntry : jenKinsViews
						.entrySet()) {
					String viewName = jenkinsViewEntry.getKey();
					List<String> jobNameList = jenkinsViewEntry.getValue();
					if (viewName != null && jobNameList != null
							&& jobNameList.size() > 0) {
						List<JenkinsJob> jobList = new ArrayList<JenkinsJob>();
						for (String name : jobNameList) {
							JenkinsJob job = getFailedJenkinsJob(name);
							if (job != null) {
								jobList.add(job);
							}
						}
						map.put(viewName, jobList);
					}
				}
			} else {
				for (Map.Entry<String, List<String>> rerunViewEntry : rerunViews
						.entrySet()) {

					if (jenKinsViews.containsKey(rerunViewEntry.getKey())) {
						List<String> rerunNames = rerunViewEntry.getValue();
						List<String> jenKinsNames = jenKinsViews
								.get(rerunViewEntry.getKey());
						// Rerun Jobs
						List<String> names = new ArrayList<String>();
						if (rerunNames == null) {
							names.addAll(jenKinsNames);
						} else {
							for (String rerunName : rerunNames) {
								if (jenKinsNames.contains(rerunName)) {
									names.add(rerunName);
								}
							}
						}

						if (!names.isEmpty()) {
							List<JenkinsJob> jobList = new ArrayList<JenkinsJob>();
							for (String name : names) {
								JenkinsJob job = getFailedJenkinsJob(name);
								if (job != null) {
									jobList.add(job);
								}
							}

							map.put(rerunViewEntry.getKey(), jobList);
						}
					}
				}
			}
		}
		return map;
	}

	public JenkinsJob getFailedJenkinsJob(String jobName) {
		String jobPath = jenKinsFolder + "\\jobs\\" + jobName;
		if (jobName != null) {
			File job = new File(jobPath);
			if (job.exists() && job.isDirectory()) {
				File jobConfig = new File(jobPath + "\\config.xml");
				if (jobConfig.exists()) {
					XMLAnalyser analyser = new XMLAnalyser(jobConfig);
					String rootPom = analyser
							.getNodeString("//maven2-moduleset/rootPOM");
					String goals = analyser
							.getNodeString("//maven2-moduleset/goals");
					if (rootPom != null && goals != null) {
						JenkinsJob jenkinsJob = new JenkinsJob();
						String[] tempArray = rootPom.split("\\\\");
						for (int i = 0; i < tempArray.length; i++) {
							String temp = tempArray[i];
							if (temp.startsWith("$")) {
								tempArray[i] = jenkinsConfig
										.getGlobalNodeProperties()
										.get(temp.substring(1, temp.length()));
							}
						}
						String realPom = tempArray[0];
						for (int i = 1; i < tempArray.length; i++) {
							realPom += "\\" + tempArray[i];
						}
						// realPom = realPom.replaceAll("\\\\", "\\\\\\\\");

						jenkinsJob.setGoals(goals);
						jenkinsJob.setJobName(jobName);
						jenkinsJob.setPomPath(realPom);
						jenkinsJob.setModules(getFaildJenkinsModules(jobPath));

						return jenkinsJob;
					}
				}
			}
		}
		return null;
	}

	public List<JenkinsModule> getFaildJenkinsModules(String jobPath) {
		String modulesPath = jobPath + "\\modules";
		File modules = new File(modulesPath);
		if (modules.exists() && modules.isDirectory()) {
			List<JenkinsModule> list = new ArrayList<JenkinsModule>();
			File[] moduleArray = modules.listFiles();
			for (File moduleFile : moduleArray) {
				JenkinsModule module = getFailedJenkinsModule(modulesPath
						+ "\\" + moduleFile.getName());
				if (module != null) {
					list.add(module);
				}
			}
			return list;
		}
		return null;
	}

	public JenkinsModule getFailedJenkinsModule(String modulePath) {
		File moduleFile = new File(modulePath);
		if (moduleFile.exists() && moduleFile.isDirectory()) {
			JenkinsModule module = new JenkinsModule();
			File nexeNumberFile = new File(modulePath + "\\nextBuildNumber");
			if (nexeNumberFile.exists()) {
				FileAnalyser analyser = new FileAnalyser(modulePath
						+ "\\nextBuildNumber");
				String nextNumber = analyser.readLine(1);
				if (nextNumber != null) {
					module.setNextBuildNumber(Integer.parseInt(nextNumber));
				}
				module.setModuleName(moduleFile.getName());
				module.setLastBuildResult(getLastFailedJenkinsJunitResult(
						modulePath, module.getNextBuildNumber()));
			}
			return module;
		}
		return null;
	}

	public int getLastJenkinsBuildFolderNumber(String modulePath, int nextNumber) {
		String filePath = null;
		nextNumber--;
		int i = nextNumber;
		for (; i >= 1; i--) {
			filePath = modulePath + "\\builds\\" + i;
			if (new File(filePath).exists() && new File(filePath).isDirectory()
					&& new File(filePath + "\\build.xml").exists()) {
				break;
			}
		}

		return i;
	}

	public JenkinsJunitResult getLastFailedJenkinsJunitResult(
			String modulePath, int nextNumber) {
		int lastBuildFolderNumber = getLastJenkinsBuildFolderNumber(modulePath,
				nextNumber);
		String lastBuildFolderPath = modulePath + "\\builds\\"
				+ lastBuildFolderNumber;
		File lastBuildFolder = new File(lastBuildFolderPath);
		if (lastBuildFolder.exists() && lastBuildFolder.isDirectory()) {
			JenkinsJunitResult unitResult = new JenkinsJunitResult();
			if (new File(lastBuildFolderPath + "\\build.xml").exists()) {
				XMLAnalyser buildXml = new XMLAnalyser(new File(
						lastBuildFolderPath + "\\build.xml"));
				String failCountStr = buildXml
						.getNodeString("//hudson.maven.MavenBuild/actions/hudson.maven.reporters.SurefireReport/failCount");
				String skipCount = buildXml
						.getNodeString("//hudson.maven.MavenBuild/actions/hudson.maven.reporters.SurefireReport/skipCount");
				String totalCount = buildXml
						.getNodeString("//hudson.maven.MavenBuild/actions/hudson.maven.reporters.SurefireReport/totalCount");

				if (!StringUtil.isNullOrEmpty(failCountStr)) {
					unitResult.setFailCount(Integer.parseInt(failCountStr
							.trim()));
				}
				if (!StringUtil.isNullOrEmpty(skipCount)) {
					unitResult.setSkipCount(Integer.parseInt(skipCount.trim()));
				}
				if (!StringUtil.isNullOrEmpty(totalCount)) {
					unitResult
							.setTotalCount(Integer.parseInt(totalCount.trim()));
				}
				unitResult.setBuildNumber(lastBuildFolderNumber);

				File junitResultXml = new File(lastBuildFolderPath
						+ "\\junitResult.xml");
				if (!junitResultXml.exists()) {
					unitResult
							.setSuites(new ArrayList<JenkinsJunitResultSuite>());
					return unitResult;
				}

				XMLAnalyser juniteAnalyer = new XMLAnalyser(junitResultXml);
				String durationStr = juniteAnalyer
						.getNodeString("//result/duration");
				unitResult.setTotalDuration(new BigDecimal(durationStr.trim()));
				String keepLongStdioStr = juniteAnalyer
						.getNodeString("//result/keepLongStdio");
				unitResult.setKeepLongStdio(Boolean
						.parseBoolean(keepLongStdioStr.trim()));
				unitResult.setSuites(getLastFailedSuites(juniteAnalyer));
			}
			return unitResult;
		}

		return null;
	}

	public List<JenkinsJunitResultSuite> getLastFailedSuites(
			XMLAnalyser juniteAnalyser) {
		if (juniteAnalyser != null) {
			List<Node> suiteList = juniteAnalyser
					.getNodeList("//result/suites/suite");
			if (suiteList != null && suiteList.size() > 0) {
				List<JenkinsJunitResultSuite> list = new ArrayList<JenkinsJunitResultSuite>();
				for (Node suiteNode : suiteList) {
					JenkinsJunitResultSuite suite = new JenkinsJunitResultSuite();
					suite.setFile(juniteAnalyser
							.getNodeString("//result/suites/suite/file"));
					suite.setSuiteName(juniteAnalyser
							.getNodeString("//result/suites/suite/name"));
					suite.setSuiteDuration(new BigDecimal(juniteAnalyser
							.getNodeString("//result/suites/suite/duration")
							.trim()));
					List<Node> caseNodes = suiteNode.selectNodes("cases/case");
					if (caseNodes != null) {
						List<JenkinsJunitResultCase> cases = new ArrayList<JenkinsJunitResultCase>();
						for (Node caseNode : caseNodes) {
							if (caseNode.selectSingleNode("skipped").getText()
									.equals("false")
									&& caseNode
											.selectSingleNode("errorStackTrace") == null) {
								continue;
							}
							JenkinsJunitResultCase jCase = new JenkinsJunitResultCase();
							String skippedStr = caseNode
									.selectSingleNode("skipped").getText()
									.trim();
							if (skippedStr.equals("false")) {
								jCase.setCaseStatus(JenkinsTestCaseStatusEnum.Failed);
								Node erroDetailNode = caseNode
										.selectSingleNode("errorDetails");
								if (erroDetailNode != null) {
									erroDetailNode.getText().trim();
								}
								if (caseNode
										.selectSingleNode("errorStackTrace") != null) {
									jCase.setErrorStackTrace(caseNode
											.selectSingleNode("errorStackTrace")
											.getText().trim());
								}
								if (caseNode.selectSingleNode("stderr") != null) {
									jCase.setStdout(caseNode
											.selectSingleNode("stderr")
											.getText().trim());
								}

							} else {
								jCase.setCaseStatus(JenkinsTestCaseStatusEnum.Skipped);
							}
							String classNameStr = caseNode
									.selectSingleNode("className").getText()
									.trim();
							jCase.setPackageName(classNameStr.substring(0,
									classNameStr.lastIndexOf(".")));
							jCase.setClassName(classNameStr.substring(
									classNameStr.lastIndexOf(".") + 1,
									classNameStr.length()));
							jCase.setTestName(caseNode
									.selectSingleNode("testName").getText()
									.trim());
							cases.add(jCase);
						}

						suite.setCases(cases);
						list.add(suite);
					}
				}
				return list;
			}
		}

		return null;
	}

}
