package com.ea.rerun.analyse.impl;

import com.ea.rerun.analyse.IAnalyse;
import com.ea.rerun.analyse.model.MavenCommends;
import com.ea.rerun.getData.model.orgData.JenkinsTestResult;

/**
 * @author Jackey jaceky90.hj@gmail.com
 * @Date May 8, 2014
 * 
 * 	analyse the jenkins test result, and generate the maven commands to rerun the failures.
 */
public class AnalyseJenkinsTestResult implements IAnalyse{
	public JenkinsTestResult jenkinsResult;

	public AnalyseJenkinsTestResult(JenkinsTestResult jenkinsResult) {
		this.jenkinsResult = jenkinsResult;
	}

	public MavenCommends jenkinsResult2MvnCmd() {
		return null;
	}
}
