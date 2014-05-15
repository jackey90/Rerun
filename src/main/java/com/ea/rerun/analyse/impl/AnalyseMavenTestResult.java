package com.ea.rerun.analyse.impl;

import com.ea.rerun.analyse.IAnalyse;
import com.ea.rerun.analyse.model.MavenRerunResultCase;
import com.ea.rerun.analyse.model.MavenRerunTestCase;
import com.ea.rerun.common.model.TestCase;

public class AnalyseMavenTestResult implements IAnalyse{
	
	
	@Override
	public Object getAnalyseData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public MavenRerunResultCase getOneResult(MavenRerunTestCase mavenRerunTestCase){
		TestCase testCase = mavenRerunTestCase.getTestCase();
		testCase.run();
		
		return null;
	}
	
	
}
