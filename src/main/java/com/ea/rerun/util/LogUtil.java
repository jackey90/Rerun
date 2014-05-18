package com.ea.rerun.util;

import java.io.File;

import com.ea.rerun.common.model.TestCase;
import com.ea.rerun.getData.model.config.RerunConfig;
import com.ea.rerun.getData.model.config.RerunLogConfig;

public class LogUtil {
	private static RerunLogConfig logConfig = RerunConfig.getInstance()
			.getLogConfig();;

	private static void copyReport(TestCase testCase, File orgReport){
		switch(logConfig.getKeepLogEnum()){
		case 
		}
	}
}
