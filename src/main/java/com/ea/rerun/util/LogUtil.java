package com.ea.rerun.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.ea.rerun.common.model.TestCase;
import com.ea.rerun.getData.model.config.KeepLogEnum;
import com.ea.rerun.getData.model.config.RerunConfig;
import com.ea.rerun.getData.model.config.RerunLogConfig;

public class LogUtil {
	private static RerunLogConfig logConfig = RerunConfig.getInstance()
			.getLogConfig();;

	public static void copyReport(TestCase testCase, File orgReport) {
		String logPath = logConfig.getLogPath();
		KeepLogEnum keepLogEnum = logConfig.getKeepLogEnum();
		int daysToKeep = logConfig.getDaysToKeep();

		deleteDayBefore(daysToKeep);
		copyAllReport(testCase,orgReport);
		
		switch (logConfig.getKeepLogEnum()) {
		case KeepAll:
			break;
		case KeepSuccess:
			break;
		case KeepFailure:
			break;
		case KeepNone:
			break;
		default:
			break;
		}
	}

	private static void deleteDayBefore(int daysToKeep) {

	}

	private static void copyAllReport(TestCase test, File orgReport) {
		File logDir = new File(logConfig.getLogPath());
		if (!logDir.exists()) {
			try {
				logDir.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File desReport = new File(logConfig.getLogPath() + "\\"
				+ test.getBranch() + "\\" + test.getPack() + "\\"
				+ test.getClassName() + "\\" + test.getTestName() + ".xml");

		try {
			FileUtils.copyFile(desReport, orgReport);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
