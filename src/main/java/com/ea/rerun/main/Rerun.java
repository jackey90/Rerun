package com.ea.rerun.main;

import java.util.ArrayList;
import java.util.List;

import com.ea.rerun.analyse.IAnalyse;
import com.ea.rerun.analyse.impl.AnalyseJenkinsTestResult;
import com.ea.rerun.analyse.model.MavenRerunTestCase;
import com.ea.rerun.feedback.IFeedBack;
import com.ea.rerun.getData.IGetData;
import com.ea.rerun.getData.impl.GetOrgData;
import com.ea.rerun.getData.model.orgData.JenkinsTestResult;
import com.ea.rerun.util.MavenUtil;

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
		for (MavenRerunTestCase cmd : list) {
			// MavenUtil.run(cmd.getCommand());
		}
		// MavenUtil.run(strList);
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
