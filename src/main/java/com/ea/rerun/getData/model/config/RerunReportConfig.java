package com.ea.rerun.getData.model.config;

import java.io.File;

import org.dom4j.Document;

import com.ea.rerun.common.util.XMLAnalyser;

public class RerunReportConfig extends XMLAnalyser {

	// singleton
	private static RerunReportConfig reportConfig;

	private final String reportTemplatePath;
	private final String reportOutPutPath;

	private RerunReportConfig(Document doc) {
		super(doc);
		reportTemplatePath = generateTemplatePath();
		reportOutPutPath = generateOutPutPath();
	}

	public static RerunReportConfig getInstance(Document doc) {
		if (reportConfig == null) {
			reportConfig = new RerunReportConfig(doc);
		}
		return reportConfig;
	}

	private String generateTemplatePath() {
		String result = getNodeString("/rerunConfig/report/ReportTemplatePath");
		if (isNullOrEmpty(result)) {
			warning("ReportTemplatePath not set, use default!");
			result = RerunConfig.currentPath + "reportTemplate.xml";

		}
		if (!new File(result).exists()) {
			result = "str\\main\\resources\\defaultReportTemplate.xml";
		}

		return result;
	}

	private String generateOutPutPath() {
		String result = getNodeString("/rerunConfig/report/ReportOutPutPath");
		if (isNullOrEmpty(result)) {
			warning("ReportOutPutPath not set, use default!");
			result = RerunConfig.currentPath + "Result.html";
		}
		return result;
	}

	public String getReportTemplatePath() {
		return reportTemplatePath;
	}

	public String getReportOutPutPath() {
		return reportOutPutPath;
	}

}
