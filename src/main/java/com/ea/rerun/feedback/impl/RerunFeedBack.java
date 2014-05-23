package com.ea.rerun.feedback.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ea.rerun.common.model.TestCase;
import com.ea.rerun.common.util.ReportFormatter;
import com.ea.rerun.feedback.IFeedBack;
import com.ea.rerun.feedback.model.ReportCell;
import com.ea.rerun.feedback.model.ReportModel;
import com.ea.rerun.feedback.model.RerunClassResult;
import com.ea.rerun.feedback.model.RerunJobResult;

/**
 * @author Jackey jaceky90.hj@gmail.com
 * @Date May 8, 2014
 * 
 *       give the feedback: generate the result.html
 */
public class RerunFeedBack implements IFeedBack {
	Map<String, Map<String, RerunJobResult>> finalResult;
	private String address = "123";

	public RerunFeedBack(Map<String, Map<String, RerunJobResult>> finalResult) {
		this.finalResult = finalResult;
	}

	public void feedBack() {
		List<ReportModel> report = toReportModel(finalResult);
		ReportFormatter formatReport = new ReportFormatter(
				report);
		String reportMessage =  formatReport.formatReport();
		System.out.println(reportMessage);
	}

	private List<ReportModel> toReportModel(
			Map<String, Map<String, RerunJobResult>> finalResult) {
		if (finalResult != null) {
			List<ReportModel> modelList = new ArrayList<ReportModel>();
			for (Map.Entry<String, Map<String, RerunJobResult>> viewEntry : finalResult
					.entrySet()) {
				String viewName = viewEntry.getKey();
				Map<String, RerunJobResult> jobResultMap = viewEntry.getValue();
				ReportModel reportModel = new ReportModel();
				Map<String, String> fields = new LinkedHashMap<String, String>();
				fields.put("Title : ", viewName);
				fields.put("URL : ", address);
				fields.put("Report Date: ", new Date().toString());
				reportModel.setFields(fields);
				List<String> headers = getHeaders(false);
				reportModel.setHeaders(headers);
				List<List<ReportCell>> bodys = new ArrayList<List<ReportCell>>();
				for (Map.Entry<String, RerunJobResult> jobResultEntry : jobResultMap
						.entrySet()) {
					List<List<ReportCell>> jobTrs = getTr(
							jobResultEntry.getValue(), false);
					bodys.addAll(jobTrs);
				}
				reportModel.setBodys(bodys);
				modelList.add(reportModel);
			}
			return modelList;
		}

		return null;
	}

	private List<String> getHeaders(boolean withBug) {
		List<String> headers = new ArrayList<String>();
		headers.add("Job");
		headers.add("Performance");
		headers.add("Class");
		headers.add("Failure Catagory");
		headers.add("Failed Number");
		headers.add("TestCase");
		headers.add("Rerun Time");
		headers.add("Rerun Command");
		if (withBug) {
			headers.add("Bug");
		}

		return headers;
	}

	private List<List<ReportCell>> getTr(RerunJobResult jobResult,
			boolean withBug) {
		if (jobResult != null) {
			List<List<ReportCell>> list = new ArrayList<List<ReportCell>>();
			List<ReportCell> jobTr = new ArrayList<ReportCell>();
			list.add(jobTr);
			ReportCell jobTd = new ReportCell("rowspan=\""
					+ jobResult.getAllCount() + "\"", "",
					jobResult.getJobName());
			jobTr.add(jobTd);
			ReportCell performanceTd = new ReportCell("rowspan=\""
					+ jobResult.getAllCount() + "\"", "",
					getPerformance(jobResult));
			jobTr.add(performanceTd);
			Map<String, RerunClassResult> classResultMap = jobResult
					.getClassResults();
			// Iterator<RerunClassResult> classResultItr =
			// classResultMap.values().iterator();
			// RerunClassResult firestClassResult = classResultItr.next();
			int classIndex = 0;
			for (Map.Entry<String, RerunClassResult> classResultEntry : classResultMap
					.entrySet()) {
				classIndex++;
				List<ReportCell> classTr = null;
				RerunClassResult classResult = classResultEntry.getValue();

				ReportCell classNameTd = new ReportCell("rowspan=\""
						+ classResult.getCount() + "\"", "",
						classResult.getClassName());
				if (classIndex == 1) {
					jobTr.add(classNameTd);
				} else {
					classTr = new ArrayList<ReportCell>();
					list.add(classTr);
					classTr.add(classNameTd);
				}

				Map<String, List<TestCase>> failureCatagoryMap = classResult
						.getFailureCatagory();
				int catagoryIndex = 0;
				for (Map.Entry<String, List<TestCase>> catagoryEntry : failureCatagoryMap
						.entrySet()) {
					catagoryIndex++;
					List<ReportCell> catagoryTr = null;

					String errorSummary = catagoryEntry.getKey();
					List<TestCase> cases = catagoryEntry.getValue();

					ReportCell failureCatagory = new ReportCell("rowspan=\""
							+ cases.size() + "\"", "", errorSummary);
					ReportCell failedNumber = new ReportCell("rowspan=\""
							+ cases.size() + "\"", "", cases.size() + "");
					if (classIndex == 1 && catagoryIndex == 1) {
						jobTr.add(failureCatagory);
						jobTr.add(failedNumber);
					} else if (classIndex != 1 && catagoryIndex == 1) {
						if (classTr != null) {
							classTr.add(failureCatagory);
							classTr.add(failedNumber);
						}
					} else if (catagoryIndex != 1) {
						catagoryTr = new ArrayList<ReportCell>();
						list.add(catagoryTr);
						catagoryTr.add(failureCatagory);
						catagoryTr.add(failedNumber);
					}

					int caseIndex = 0;
					for (TestCase testCase : cases) {
						caseIndex++;
						List<ReportCell> caseTr = null;

						ReportCell caseName = new ReportCell("rowspan=\"" + 1
								+ "\"", "", testCase.getTestName());
						ReportCell rerunTimes = new ReportCell("rowspan=\"" + 1
								+ "\"", "", testCase.getResult().getRunCount()
								+ "");
						if (classIndex == 1 && catagoryIndex == 1
								&& caseIndex == 1) {
							jobTr.add(caseName);
							jobTr.add(rerunTimes);
							ReportCell mvnCommand = new ReportCell("rowspan=\""
									+ jobResult.getAllCount() + "\"", "",
									testCase.getMavenCommand());
							jobTr.add(mvnCommand);
						} else if (classIndex == 1 && catagoryIndex != 1
								&& caseIndex == 1) {
							if (catagoryTr != null) {
								catagoryTr.add(caseName);
								catagoryTr.add(rerunTimes);
							}
						} else if (classIndex != 1 && catagoryIndex == 1
								&& caseIndex == 1) {
							if (classTr != null) {
								classTr.add(caseName);
								classTr.add(rerunTimes);
							}
						} else if (classIndex != 1 && catagoryIndex != 1
								&& caseIndex == 1) {
							if (catagoryTr != null) {
								catagoryTr.add(caseName);
								catagoryTr.add(rerunTimes);
							}
						} else if (caseIndex != 1) {
							caseTr = new ArrayList<ReportCell>();
							list.add(caseTr);
							caseTr.add(caseName);
							caseTr.add(rerunTimes);
						}
					}
				}

			}
			return list;
		}

		return null;
	}

	private String getPerformance(RerunJobResult jobResult) {
		if (jobResult != null) {
			return "Total: " + jobResult.getAllCount() + "\n" + "Failed :"
					+ jobResult.getFailureCount() + "\n" + "Error :"
					+ jobResult.getErrorCount() + "\n" + "Skiped :"
					+ jobResult.getSkipCount() + "\n" + "Successed :"
					+ jobResult.getSuccessCount();
		}
		return null;
	}

}
