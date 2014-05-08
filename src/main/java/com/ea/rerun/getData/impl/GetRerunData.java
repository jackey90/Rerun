package com.ea.rerun.getData.impl;

import com.ea.rerun.getData.IGetData;
import com.ea.rerun.getData.model.JenkinsTestResult;
import com.ea.rerun.getData.model.RerunConfig;

/**
 * @author Jackey jaceky90.hj@gmail.com
 * @Date May 8, 2014
 * 
 *       get JenkinsTestResult according to the RerunConfig
 */
public class GetRerunData implements IGetData{
	private RerunConfig config;

	public GetRerunData(RerunConfig config) {
		this.config = config;
	}

	public JenkinsTestResult getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
