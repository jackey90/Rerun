package com.ea.rerun.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportFormatter {
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

	private String patternStringForPlaceHolderHasBody = "%s\\{(.*?)\\}%s";

	public ReportFormatter(String templateFile) {
		this.templateFile = templateFile;
	}

	public String formatReport(List<ReportSection> reportSectionList) {
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

		Entry<String, Integer> reportSectionTemplate = getUniqueBodyPlaceHolder(
				reportSectionBodyPlaceHolderName, templateContent);
		Entry<String, Integer> headFieldTemplate = getUniqueBodyPlaceHolder(
				fieldBodyPlaceHolderName, reportSectionTemplate.key);
		Entry<String, Integer> tableHeadRowTemplate = getUniqueBodyPlaceHolder(
				tableHeadRowBodyPlaceHolderName, reportSectionTemplate.key);
		Entry<String, Integer> tableHeadCellTemplate = getUniqueBodyPlaceHolder(
				tableHeadCellBodyPlaceHolderName, reportSectionTemplate.key);
		Entry<String, Integer> contentRowTemplate = getUniqueBodyPlaceHolder(
				tableContentRowPlaceHolderName, reportSectionTemplate.key);
		Entry<String, Integer> contentCellTemplate = getUniqueBodyPlaceHolder(
				tableContentCellBodyPlaceHolderName, contentRowTemplate.key);

		// report section heads.
		StringBuilder reportSections = new StringBuilder();
		for (ReportSection reportSection : reportSectionList) {
			StringBuilder headFields = new StringBuilder();
			for (Entry<String, String> headField : reportSection.headFields) {
				headFields.append(headFieldTemplate.key.replace(
						fieldNamePlaceHolderName, headField.key).replace(
						fieldValuePlaceHolderName, headField.value));
			}

			// table head row.
			StringBuilder columnsBuilder = new StringBuilder();
			for (String columnName : reportSection.columnNames) {
				columnsBuilder.append(tableHeadCellTemplate.key.replace(
						tableColumnNamePlaceHolderName, columnName));
			}
			String tableHeadRow = replacePlaceHolderBody(
					tableHeadRowTemplate.key, tableHeadCellBodyPlaceHolderName,
					columnsBuilder.toString());

			// table common rows.
			StringBuilder contentRows = new StringBuilder();
			for (List<ContentCell> reportItem : reportSection.items) {
				StringBuilder currentRow = new StringBuilder();
				for (ContentCell cell : reportItem) {
					currentRow.append(contentCellTemplate.key
							.replace(tableCellValuePlaceHolderName, cell.value)
							.replace(tableContentCellAdditionalAttrs,
									cell.cellAdditionalAttribute)
							.replace(tableTdAdditionalAttrs,
									cell.TdAdditionalAttribute));
				}
				contentRows.append(replacePlaceHolderBody(
						contentRowTemplate.key,
						tableContentCellBodyPlaceHolderName,
						currentRow.toString()));
			}

			String afterPrepareHeads = replacePlaceHolderBody(
					reportSectionTemplate.key, fieldBodyPlaceHolderName,
					headFields.toString());
			String afterPrepareTableHeads = replacePlaceHolderBody(
					afterPrepareHeads, tableHeadRowBodyPlaceHolderName,
					tableHeadRow);
			reportSections.append(replacePlaceHolderBody(
					afterPrepareTableHeads, tableContentRowPlaceHolderName,
					contentRows.toString()));
		}

		return replacePlaceHolderBody(templateContent,
				reportSectionBodyPlaceHolderName, reportSections.toString());
	}

	private String replacePlaceHolderBody(String context,
			String placeHolderName, String contentWillBeUsedToReplace) {
		Pattern patternForPlaceHolderHasBody = Pattern.compile(String.format(
				patternStringForPlaceHolderHasBody, placeHolderName,
				placeHolderName), Pattern.DOTALL);
		Matcher m = patternForPlaceHolderHasBody.matcher(context);
		StringBuilder result = new StringBuilder(context);
		while (m.find()) {
			result.replace(m.start(), m.end(), contentWillBeUsedToReplace);
		}
		return result.toString();
	}

	private Entry<String, Integer> getUniqueBodyPlaceHolder(
			String placeHolderName, String templateContent) {
		List<Entry<String, Integer>> reportSectionPlaceHolders = getContentForBoyPlaceHolder(
				placeHolderName, templateContent);
		if (reportSectionPlaceHolders.size() != 1) {
			throw new IllegalStateException("The template file " + templateFile
					+ " is not expected have and only have one place holder "
					+ placeHolderName);
		}
		return reportSectionPlaceHolders.get(0);
	}

	// bodyPlaceHolderName{<repeatContent>}bodyPlaceHolderName;
	private List<Entry<String, Integer>> getContentForBoyPlaceHolder(
			String placeHolderName, String templateContent) {
		List<Entry<String, Integer>> result = new ArrayList<Entry<String, Integer>>();
		Pattern patternForPlaceHolderHasBody = Pattern.compile(String.format(
				patternStringForPlaceHolderHasBody, placeHolderName,
				placeHolderName), Pattern.DOTALL);
		Matcher m = patternForPlaceHolderHasBody.matcher(templateContent);
		while (m.find()) {
			Entry<String, Integer> matchPart = new Entry<String, Integer>(
					m.group(1), m.start());
			result.add(matchPart);
		}
		return result;
	}
}
