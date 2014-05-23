package com.ea.rerun.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
		copyReportAll(testCase, orgReport);

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

	private static void copyReportAll(TestCase test, File orgReport) {
		File desReport = new File(logConfig.getLogPath() + "\\"
				+ test.getBranch() + "\\" + test.getPack() + "\\"
				+ test.getClassName() + "\\" + test.getTestName() + ".xml");
		File desReportDir = new File(logConfig.getLogPath() + "\\"
				+ test.getBranch() + "\\" + test.getPack() + "\\"
				+ test.getClassName());
		try {
			if (!desReportDir.exists()) {
				FileUtils.forceMkdir(desReportDir);
			}
			int byteread = 0;
			if (orgReport.exists()) {

				if (desReport.exists()) {
					desReport.delete();
				}
				InputStream inStream = new FileInputStream(orgReport);
				FileOutputStream fs = new FileOutputStream(desReport, false);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			PrintUtil.warning("Fail to generate report " + desReport.getName());
			e.printStackTrace();
		}
	}
}
