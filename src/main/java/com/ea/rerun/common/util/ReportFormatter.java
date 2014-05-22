package com.ea.rerun.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ea.rerun.feedback.model.RerunJobResult;

public class ReportFormatter {
	private Map<String, Map<String, RerunJobResult>> finalResult;

	private String templateFile = "";

	private final String reportSectionBodyPlaceHolderName = "###ReportSection###";

	private final String fieldBodyPlaceHolderName = "###Field###";
	private final String fieldNamePlaceHolderName = "###FieldName###";
	private final String fieldValuePlaceHolderName = "###FieldValue###";

	private final String tableHeadRowBodyPlaceHolderName = "###TableHeadRow###";
	private final String tableHeadCellBodyPlaceHolderName = "###TableHeadCell###";
	private final String tableColumnNamePlaceHolderName = "###ColumnName###";

	private final String tableContentRowPlaceHolderName = "###TableContentRow###";
	private final String tableContentCellBodyPlaceHolderName = "###TableContentCell###";
	private final String tableCellValuePlaceHolderName = "###CellValue###";

	private final String tableContentCellAdditionalAttrs = "###CELL_A_ATTRS###";
	private final String tableTdAdditionalAttrs = "###TD_ATTRS###";

	private String templatePattern = "%s\\{(.*?)\\}%s";

	public ReportFormatter(String templateFile) {
		this.templateFile = templateFile;
	}

	public void Job2Tr(RerunJobResult job) {

	}

	public String formatReport() {
		String templateContent = "";
		try {
			File file = new File(templateFile);
			StringBuffer buffer = new StringBuffer();
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), "utf-8");
			int s;
			while ((s = isr.read()) != -1) {
				buffer.append((char) s);
			}
			templateContent = buffer.toString();

			isr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		String reportSectionTemplate = getReportTemplate(
				reportSectionBodyPlaceHolderName, templateContent);
		String headFieldTemplate = getReportTemplate(fieldBodyPlaceHolderName,
				reportSectionTemplate);
		String tableHeadRowTemplate = getReportTemplate(
				tableHeadRowBodyPlaceHolderName, reportSectionTemplate);
		String tableHeadCellTemplate = getReportTemplate(
				tableHeadCellBodyPlaceHolderName, reportSectionTemplate);
		String contentRowTemplate = getReportTemplate(
				tableContentRowPlaceHolderName, reportSectionTemplate);
		String contentCellTemplate = getReportTemplate(
				tableContentCellBodyPlaceHolderName, contentRowTemplate);
		for (Map.Entry<String, Map<String, RerunJobResult>> entry : finalResult
				.entrySet()) {
			String viewName = entry.getKey();

			Map<String,RerunJobResult> jobMap = entry.getValue();
			String contenRowTemplate = "";
			int jobIndex = 0;
			for(Map.Entry<String,RerunJobResult> jobEntry: jobMap.entrySet()){
				
			}
			
		}

		return templateContent;

	}

	private String getReportTemplate(String placeHolderName,
			String templateContent) {
		String result = "";
		Pattern patternForPlaceHolderHasBody = Pattern.compile(String.format(
				templatePattern, placeHolderName, placeHolderName),
				Pattern.DOTALL);
		Matcher m = patternForPlaceHolderHasBody.matcher(templateContent);
		if (m.find()) {
			result = m.group(1);
		}
		return result;
	}
}
