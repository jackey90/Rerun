package com.ea.rerun.main;

import java.util.List;

import com.ea.rerun.analyse.IAnalyse;
import com.ea.rerun.analyse.impl.AnalyseJenkinsTestResult;
import com.ea.rerun.analyse.model.MavenCommand;
import com.ea.rerun.feedback.IFeedBack;
import com.ea.rerun.getData.IGetData;
import com.ea.rerun.getData.impl.GetRerunData;
import com.ea.rerun.getData.model.orgData.JenkinsTestResult;

public class Rerun {
	private IGetData getRerunData;
	private IAnalyse analyseData;
	private IFeedBack rerunFeedBack;

	
	public Rerun(){
		getRerunData = new GetRerunData();
		analyseData = new AnalyseJenkinsTestResult(getJenkinsTestResult());
	}
	
	public void setAnalyseData(IAnalyse analyseData) {
		this.analyseData = analyseData;
		
	}
	
	public JenkinsTestResult getJenkinsTestResult(){
		return (JenkinsTestResult) getRerunData.getData();
	}
	
	public static void main(String[] args) {
		Rerun r = new Rerun();
		JenkinsTestResult result = r.getJenkinsTestResult();
		AnalyseJenkinsTestResult an = new AnalyseJenkinsTestResult(result);
		List<MavenCommand> list = an.jenkinsResult2MvnCmd();
		System.out.println();
	}
	

}
