package com.ea.rerun.getData.impl;

import com.ea.rerun.getData.model.RerunConfig;

/**
 * @author Jackey jaceky90.hj@gmail.com
 * @Date May 8, 2014
 * 
 *  get the rerunConfig.xml, and translate it to RerunConfig
 */
public class GetRerunConfig {
	private String configPath;
	

	public GetRerunConfig(){
		configPath = "rerunConfig.xml";
	}
	
	private GetRerunConfig(String configPath) {
		this.configPath = configPath;
	}
	
	public RerunConfig getConfig(){
		if(configPath != null){
			
		}
		
		return null;
	}
}
