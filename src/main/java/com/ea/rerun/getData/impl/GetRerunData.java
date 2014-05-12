package com.ea.rerun.getData.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.ea.rerun.getData.IGetData;
import com.ea.rerun.getData.model.config.JenkinsRootConfig;
import com.ea.rerun.getData.model.config.RerunConfig;
import com.ea.rerun.getData.model.orgData.JenkinsJob;
import com.ea.rerun.getData.model.orgData.JenkinsJunitResult;
import com.ea.rerun.getData.model.orgData.JenkinsModule;
import com.ea.rerun.getData.model.orgData.JenkinsTestResult;
import com.ea.rerun.util.FileAnalyser;
import com.ea.rerun.util.PrintUtil;
import com.ea.rerun.util.StringUtil;
import com.ea.rerun.util.XMLAnalyser;

/**
 * @author Jackey jaceky90.hj@gmail.com
 * @Date May 8, 2014
 * 
 *       get JenkinsTestResult according to the RerunConfig
 */
public class GetRerunData implements IGetData {
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
		}

		return null;
	}

	public Map<String, List<JenkinsJob>> getFailedJenkinsTestViews() {
		Map<String, List<JenkinsJob>> map = new HashMap<String, List<JenkinsJob>>();
		Map<String, List<String>> rerunViews = rerunConfig.getJenkinsConfig()
				.getViews();
		Map<String, List<String>> jenKinsViews = jenkinsConfig.getViews();
		if (jenKinsViews.isEmpty()) {
			PrintUtil.error("The jenkins views is empty!");
			return null;
		} else {
			for (Map.Entry<String, List<String>> rerunViewEntry : rerunViews
					.entrySet()) {
				if (jenKinsViews.containsKey(rerunViewEntry.getKey())) {
					List<String> rerunNames = rerunViewEntry.getValue();
					List<String> jenKinsNames = jenKinsViews.get(rerunViewEntry
							.getKey());
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
						for (String temp : tempArray) {
							if (temp.startsWith("$")) {
								temp = jenkinsConfig.getGlobalNodeProperties()
										.get(temp.substring(1, temp.length()));
							}
						}
						String realPom = tempArray[0];
						for (int i = 1; i < tempArray.length; i++) {
							realPom += "\\" + tempArray[i];
						}

						jenkinsJob.setGoals(goals);
						jenkinsJob.setJobName(jobName);
						jenkinsJob.setPomPath(realPom);
						jenkinsJob.setModules(getFaildJenkinsModules(jobPath));
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
		return null;
	}

	public JenkinsJunitResult getLastFailedJenkinsJunitResult(
			String modulePath, int nextNumber) {
		File lastBuildFolder = null;
		String filePath = null;
		nextNumber--;
		int i = nextNumber;
		for (; i >= 1; i--) {
			filePath = modulePath + "\\builds\\" + nextNumber;
			if (new File(filePath).exists() && new File(filePath).isDirectory()) {
				lastBuildFolder = new File(filePath);
				break;
			}
		}

		if (lastBuildFolder != null) {
			JenkinsJunitResult unitResult = new JenkinsJunitResult();
			
			XMLAnalyser buildXml = new XMLAnalyser(new File(filePath
					+ "\\build.xml"));
			String failCountStr = buildXml
					.getNodeString("//hudson.maven.MavenBuild/actions/hudson.maven.reporters.SurefireReport/failCount");
			String skipCount = buildXml
					.getNodeString("//hudson.maven.MavenBuild/actions/hudson.maven.reporters.SurefireReport/skipCount");
			String totalCount = buildXml
					.getNodeString("//hudson.maven.MavenBuild/actions/hudson.maven.reporters.SurefireReport/totalCount");
			
			if(StringUtil.isNullOrEmpty(failCountStr)){
				unitResult.setFailCount(Integer.parseInt(failCountStr.trim()));
			}
			if(StringUtil.isNullOrEmpty(skipCount)){
				unitResult.setSkipCount(Integer.parseInt(skipCount.trim()));
			}
			if(StringUtil.isNullOrEmpty(totalCount)){
				unitResult.setTotalCount(Integer.parseInt(totalCount.trim()));
			}
			unitResult.setBuildNumber(i);
			
			
			XMLAnalyser juniteAnalyer = new XMLAnalyser(new File(filePath+"\\junitResult.xml"));
			
		}

		return null;
	}

}
