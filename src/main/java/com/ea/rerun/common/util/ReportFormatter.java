package com.ea.rerun.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ea.rerun.feedback.model.ReportCell;
import com.ea.rerun.feedback.model.ReportModel;
import com.ea.rerun.getData.model.config.RerunConfig;

public class ReportFormatter {
	private String reportPath = RerunConfig.getInstance().getReportConfig()
			.getReportOutPutPath();
	private String templateFile = RerunConfig.getInstance().getReportConfig()
			.getReportTemplatePath();
	List<ReportModel> report;

	private final String reportSectionHolder = "###ReportSection###";

	private final String fieldHolder = "###Field###";
	private final String fieldNameHolder = "###FieldName###";
	private final String fieldValueHolder = "###FieldValue###";

	private final String tableHeadRowHolder = "###TableHeadRow###";
	private final String tableHeadCellHolder = "###TableHeadCell###";
	private final String columnNameHolder = "###ColumnName###";

	private final String tableContentRowHolder = "###TableContentRow###";
	private final String tableContentCellHolder = "###TableContentCell###";
	private final String cellValueHolder = "###CellValue###";

	private final String cellAttrHolder = "###CELL_A_ATTRS###";
	private final String tdAttrHolder = "###TD_ATTRS###";

	private String templatePattern = "%s\\{(.*?)\\}%s";

	String reportSectionTemplate;
	String headFieldTemplate;
	String tableHeadRowTemplate;
	String tableHeadCellTemplate;
	String contentRowTemplate;
	String contentCellTemplate;

	// "C:\\Users\\chaoyuan\\Desktop\\Rerun\\src\\main\\resources\\defaultReportTemplate.xml",
	public ReportFormatter(List<ReportModel> report) {
		this.report = report;
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

		reportSectionTemplate = getReportTemplate(reportSectionHolder,
				templateContent, 0);
		headFieldTemplate = getReportTemplate(fieldHolder,
				reportSectionTemplate, 0);
		tableHeadRowTemplate = getReportTemplate(tableHeadRowHolder,
				reportSectionTemplate, 0);
		tableHeadCellTemplate = getReportTemplate(tableHeadCellHolder,
				reportSectionTemplate, 0);
		contentRowTemplate = getReportTemplate(tableContentRowHolder,
				reportSectionTemplate, 0);
		contentCellTemplate = getReportTemplate(tableContentCellHolder,
				contentRowTemplate, 0);

		StringBuffer sb = new StringBuffer();
		if (report != null) {
			for (ReportModel reportModel : report) {
				sb.append(getReportBody(reportModel));
			}
		}
		return templateContent.replace(reportSectionTemplate, sb.toString());
	}

	private String getReportBody(ReportModel reportModel) {
		String reportSectionContent = getReportTemplate(reportSectionHolder,
				reportSectionTemplate, 1);

		String headField = getFeild(reportModel.getFields());
		String tableHeader = getHeader(reportModel.getHeaders());
		String tableContent = getTableContent(reportModel.getBodys());

		return reportSectionContent.replace(headFieldTemplate, headField)
				.replace(tableHeadRowTemplate, tableHeader)
				.replace(contentRowTemplate, tableContent);
	}

	private String getFeild(Map<String, String> fields) {
		String headFieldContent = getReportTemplate(fieldHolder,
				headFieldTemplate, 1);
		StringBuffer headFields = new StringBuffer();
		for (Map.Entry<String, String> field : fields.entrySet()) {
			headFields.append(headFieldContent.replaceAll(fieldNameHolder,
					field.getKey()).replaceAll(fieldValueHolder,
					field.getValue()));
		}
		return headFields.toString();
	}

	private String getHeader(List<String> list) {
		String headerRowContent = getReportTemplate(tableHeadRowHolder,
				tableHeadRowTemplate, 1);
		String tableHeadCellContent = getReportTemplate(tableHeadCellHolder,
				tableHeadCellTemplate, 1);
		StringBuffer header = new StringBuffer();
		for (String str : list) {
			header.append(tableHeadCellContent.replace(columnNameHolder, str));
		}
		return headerRowContent.replace(tableHeadCellTemplate,
				header.toString());
	}

	private String getTableContent(List<List<ReportCell>> list) {
		String tableRowContent = getReportTemplate(tableContentRowHolder,
				contentRowTemplate, 1);
		StringBuffer tableContent = new StringBuffer();
		for (List<ReportCell> cellList : list) {
			String rowCells = getTableRowCells(cellList);
			tableContent.append(tableRowContent.replace(contentCellTemplate,
					rowCells));
		}

		return tableContent.toString();
	}

	private String getTableRowCells(List<ReportCell> cellList) {
		StringBuffer tableRow = new StringBuffer();
		String tableCellContent = getReportTemplate(tableContentCellHolder,
				contentCellTemplate, 1);
		for (ReportCell cell : cellList) {
			tableRow.append(tableCellContent
					.replace(tdAttrHolder, cell.getTdAttrs())
					.replace(cellAttrHolder, cell.getCellAAttrs())
					.replace(cellValueHolder, cell.getCellValue()));
		}

		return tableRow.toString();
	}

	private String getReportTemplate(String placeHolderName,
			String templateContent, int i) {
		String result = "";
		Pattern patternForPlaceHolderHasBody = Pattern.compile(String.format(
				templatePattern, placeHolderName, placeHolderName),
				Pattern.DOTALL);
		Matcher m = patternForPlaceHolderHasBody.matcher(templateContent);
		if (m.find()) {
			result = m.group(i);
		}
		return result;
	}
}
