package com.ea.rerun.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.ea.rerun.analyse.model.RerunResultStatusEnun;
import com.ea.rerun.common.model.TestCase;
import com.ea.rerun.common.model.TestResultType;
import com.ea.rerun.getData.model.config.KeepLogEnum;
import com.ea.rerun.getData.model.config.RerunConfig;
import com.ea.rerun.getData.model.config.RerunLogConfig;

public class LogUtil {
	private static RerunLogConfig logConfig = RerunConfig.getInstance()
			.getLogConfig();

	public static void copyReport(TestCase testCase, File orgReport) {
		String logPath = logConfig.getLogPath();
		KeepLogEnum keepLogEnum = logConfig.getKeepLogEnum();
		int daysToKeep = logConfig.getDaysToKeep();

		deleteDayBefore(daysToKeep);
		copyReport(testCase, orgReport, logConfig.getKeepLogEnum());
	}

	// TODO
	private static void deleteDayBefore(int daysToKeep) {
		
	}

	private static void copyReport(TestCase test, File orgReport,
			KeepLogEnum keepLogEnum) {
		switch (keepLogEnum) {
		case KeepAll:
			break;
		case KeepNone:
			return;
		case KeepFailure:
			if (!test.getResult().getResultType()
					.equals(TestResultType.Successed)) {
				break;
			} else {
				return;
			}
		case KeepSuccess:
			if (test.getResult().getResultType()
					.equals(TestResultType.Successed)) {
				break;
			} else {
				return;
			}
		default:
			return;
		}

		File desReportDir = new File(logConfig.getLogPath() + "\\"
				+ test.getBranch() + "\\" + test.getPack() + "."
				+ test.getClassName() + "\\" + test.getTestName() + "\\builds");
		File desReport = null;
		try {
			if (!desReportDir.exists()) {
				FileUtils.forceMkdir(desReportDir);
			}
			int nextNumber = getNextBuildNumber(desReportDir);
			String reportDir = desReportDir.getAbsolutePath() + "\\"
					+ nextNumber;
			FileUtils.forceMkdir(new File(reportDir));

			desReport = new File(reportDir + "\\" + test.getTestName() + ".xml");
			desReport.createNewFile();
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

				// increase the nextNumber
				File nextNumberFile = new File(desReportDir.getAbsolutePath()
						+ "\\nextNumber");
				FileAnalyser analyser = new FileAnalyser(nextNumberFile);
				analyser.write(nextNumber + 1 + "");

				test.getResult().setBuildCount(nextNumber);
			}
		} catch (Exception e) {
			PrintUtil.warning("Fail to generate report !");
			e.printStackTrace();
		}
	}

	private static int getNextBuildNumber(File desReportDir) {
		FileAnalyser analyser = null;
		File nextNumberFile = new File(desReportDir.getAbsolutePath()
				+ "\\nextNumber");
		if (!desReportDir.exists()) {
			try {
				FileUtils.forceMkdir(desReportDir);
				if (!nextNumberFile.createNewFile()) {
					PrintUtil.warning("Failure to create nextNumber file!");
				} else {
					analyser = new FileAnalyser(nextNumberFile);
					analyser.write("1");
					return 1;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (analyser == null) {
			if (!nextNumberFile.exists()) {
				try {
					if (!nextNumberFile.createNewFile()) {
						PrintUtil.error("Failure to create nextNumber file!");
					} else {
						analyser = new FileAnalyser(nextNumberFile);
						analyser.write("1");
						return 1;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			analyser = new FileAnalyser(nextNumberFile);
		}

		String nextNoStr = analyser.readLine(1);
		if (nextNoStr == null) {
			PrintUtil.error("Nothing in the file nextNumber");
		}
		return Integer.parseInt(nextNoStr.trim());

	}
}
