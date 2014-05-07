package com.ea.rerun.getData;

import com.ea.rerun.getData.model.RerunConfig;

/**
 * @author Jackey jaceky90.hj@gmail.com
 * @Date May 7, 2014
 * 
 */
public abstract class AbstractGetRerunData implements IGetData{
	private RerunConfig config;
	
	public AbstractGetRerunData(RerunConfig config){
		this.config = config;
	}

	
}
