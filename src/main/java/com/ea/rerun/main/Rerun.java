package com.ea.rerun.main;

import com.ea.rerun.analyse.IAnalyse;
import com.ea.rerun.feedback.IFeedBack;
import com.ea.rerun.getData.IGetData;
import com.ea.rerun.getData.impl.GetRerunData;
import com.ea.rerun.getData.model.config.RerunConfig;
import com.ea.rerun.getData.model.orgData.JenkinsTestResult;

public class Rerun {
	private IGetData getRerunData;
	private IAnalyse analyseData;
	private IFeedBack rerunFeedBack;

	
	public Rerun(){
		getRerunData = new GetRerunData();
		
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
		System.out.println();
	}
	

}
