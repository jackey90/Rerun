package com.ea.rerun.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ea.rerun.analyse.IAnalyse;
import com.ea.rerun.analyse.impl.AnalyseJenkinsTestResult;
import com.ea.rerun.analyse.model.MavenRerunTestCase;
import com.ea.rerun.common.model.TestCase;
import com.ea.rerun.feedback.IFeedBack;
import com.ea.rerun.getData.IGetData;
import com.ea.rerun.getData.impl.GetOrgData;
import com.ea.rerun.getData.model.orgData.JenkinsTestResult;

public class Rerun {
	private IGetData getRerunData;
	private IAnalyse analyseData;
	private IFeedBack rerunFeedBack;

	public Rerun() {
		getRerunData = new GetOrgData();
		analyseData = new AnalyseJenkinsTestResult(getJenkinsTestResult());
	}

	public void setAnalyseData(IAnalyse analyseData) {
		this.analyseData = analyseData;

	}

	public JenkinsTestResult getJenkinsTestResult() {
		return (JenkinsTestResult) getRerunData.getData();
	}

	public static void main(String[] args) {
		Rerun r = new Rerun();
		JenkinsTestResult result = r.getJenkinsTestResult();
		AnalyseJenkinsTestResult an = new AnalyseJenkinsTestResult(result);
		List<MavenRerunTestCase> list = an.getAnalyseData();
		List<String> strList = new ArrayList<String>();
		int i = 1;
		Map<String, List<TestCase>> map = new HashMap<String, List<TestCase>>();
		for (MavenRerunTestCase cmd : list) {
			if (cmd.getJobName().equals("rating-Comprehensive")) {
				System.out.print("");
			}
			System.out.println(cmd.getTestCase().toString());
			System.out.println(cmd.getTestCase().getMavenCommand());
			cmd.getTestCase().run();
			
			
			if (map.containsKey(cmd.getJobName())) {
				List<TestCase> caseList = map.get(cmd.getJobName());
				if (caseList == null) {
					caseList = new ArrayList<TestCase>();
				}
				caseList.add(cmd.getTestCase());
			} else {
				List<TestCase> caseList = new ArrayList<TestCase>();
				caseList.add(cmd.getTestCase());
				map.put(cmd.getJobName(), caseList);
			}
		}

		
		
		
		System.out.println();
	}

	public IGetData getGetRerunData() {
		return getRerunData;
	}

	public void setGetRerunData(IGetData getRerunData) {
		this.getRerunData = getRerunData;
	}

	public IFeedBack getRerunFeedBack() {
		return rerunFeedBack;
	}

	public void setRerunFeedBack(IFeedBack rerunFeedBack) {
		this.rerunFeedBack = rerunFeedBack;
	}

	public IAnalyse getAnalyseData() {
		return analyseData;
	}

}
