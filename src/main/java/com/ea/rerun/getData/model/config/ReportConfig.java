package com.ea.rerun.getData.model.config;

import java.io.File;

import org.dom4j.Document;

public class ReportConfig extends AbstractConfig {

	// singleton
	private static ReportConfig reportConfig;

	private final String reportTemplatePath;
	private final String reportOutPutPath;

	private ReportConfig(Document doc) {
		super(doc);
		reportTemplatePath = generateTemplatePath();
		reportOutPutPath = generateOutPutPath();
	}

	public static ReportConfig getInstance(Document doc) {
		if (reportConfig == null) {
			reportConfig = new ReportConfig(doc);
		}
		return reportConfig;
	}

	private String generateTemplatePath() {
		String result = getNodeString("/rerunConfig/report/ReportTemplatePath");
		if (result == null) {
			warning("ReportTemplatePath not set, use default!");
			result = RerunConfig.currentPath + "reportTemplate.xml";
		}
		if (!new File(result).exists()) {
			error("reportTemplate.xml does not exist!");
			return null;
		}

		return result;
	}

	private String generateOutPutPath() {
		String result = getNodeString("/rerunConfig/report/ReportOutPutPath");
		if (result == null) {
			warning("ReportOutPutPath not set, use default!");
			result = RerunConfig.currentPath + "Result.html";
		}
		if (!new File(result).exists()) {
			error("Result.html does not exist!");
			return null;
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
