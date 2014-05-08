package com.ea.rerun.main;

import com.ea.rerun.analyse.IAnalyse;
import com.ea.rerun.feedback.IFeedBack;
import com.ea.rerun.getData.IGetData;
import com.ea.rerun.getData.impl.GetRerunData;
import com.ea.rerun.getData.model.JenkinsTestResult;

public class Rerun {
	private IGetData getRerunData;
	private IAnalyse analyseData;
	private IFeedBack rerunFeedBack;

	public Rerun(){
		
	}
	
	public void setAnalyseData(IAnalyse analyseData) {
		this.analyseData = analyseData;
	}
	
	public JenkinsTestResult getJenkinsTestResult(){
		return (JenkinsTestResult) getRerunData.getData();
	}
	

}
