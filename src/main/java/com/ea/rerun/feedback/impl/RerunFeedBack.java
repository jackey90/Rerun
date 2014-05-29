package com.ea.rerun.feedback.impl;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.bcel.util.ClassPath;
import org.apache.commons.io.FileUtils;
import org.apache.regexp.REProgram;

import com.ea.rerun.common.model.TestCase;
import com.ea.rerun.common.util.PrintUtil;
import com.ea.rerun.common.util.ReportFormatter;
import com.ea.rerun.feedback.IFeedBack;
import com.ea.rerun.feedback.model.ReportCell;
import com.ea.rerun.feedback.model.ReportModel;
import com.ea.rerun.feedback.model.RerunClassResult;
import com.ea.rerun.feedback.model.RerunJobResult;
import com.ea.rerun.getData.model.config.RerunConfig;

/**
 * @author Jackey jaceky90.hj@gmail.com
 * @Date May 8, 2014
 * 
 *       give the feedback: generate the result.html
 */
public class RerunFeedBack implements IFeedBack {
	Map<String, Map<String, RerunJobResult>> finalResult;
	private String url = "";
	private String reportPath;
	private String logPath;
	private String reportDir;

	public RerunFeedBack(Map<String, Map<String, RerunJobResult>> finalResult) {
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			String address = addr.getHostAddress();
			url = "<a" + "http://" + address + ":8080" + ">" + "http://"
					+ address + ":8080" + "</a>";
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		reportPath = RerunConfig.getInstance().getReportConfig()
				.getReportOutPutPath();
		reportDir = reportPath.substring(0, reportPath.lastIndexOf("\\"));
		logPath = RerunConfig.getInstance().getLogConfig().getLogPath();
		this.finalResult = finalResult;
	}

	public void feedBack() {
		List<ReportModel> report = toReportModel(finalResult);
		reportModel2Report(report, reportPath);
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
				fields.put("URL : ", url);
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
		headers.add("Class");

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
						+ classResult.getCount() + "\"", "href=\""
						+ classResult.getClassName() + ".html"
						+ "\" target=\"details\"", classResult.getClassName());
				if (classIndex == 1) {
					jobTr.add(classNameTd);
				} else {
					classTr = new ArrayList<ReportCell>();
					list.add(classTr);
					classTr.add(classNameTd);
				}

				classResult2Report(classResult);

				// Map<String, List<TestCase>> failureCatagoryMap = classResult
				// .getFailureCatagory();
				// int catagoryIndex = 0;
				// for (Map.Entry<String, List<TestCase>> catagoryEntry :
				// failureCatagoryMap
				// .entrySet()) {
				// catagoryIndex++;
				// List<ReportCell> catagoryTr = null;
				//
				// String errorSummary = catagoryEntry.getKey();
				// List<TestCase> cases = catagoryEntry.getValue();
				//
				// ReportCell failureCatagory = new ReportCell("rowspan=\""
				// + cases.size() + "\"", "", errorSummary);
				//
				// int caseIndex = 0;
				// for (TestCase testCase : cases) {
				// caseIndex++;
				// List<ReportCell> caseTr = null;
				// String caseLogPath = classLogPath + "\\"
				// + testCase.getTestName() + "\\builds\\"
				// + testCase.getResult().getBuildCount();
				//
				// ReportCell caseName = new ReportCell("rowspan=\"" + 1
				// + "\"", "target=\"_blank\" href=\""
				// + caseLogPath + "\"", testCase.getTestName());
				// ReportCell rerunTimes = new ReportCell("rowspan=\"" + 1
				// + "\"", "", testCase.getResult().getRunCount()
				// + "");
				// if (classIndex == 1 && caseIndex == 1) {
				// jobTr.add(caseName);
				// jobTr.add(rerunTimes);
				//
				// } else if (classIndex != 1 && caseIndex == 1) {
				// if (classTr != null) {
				// classTr.add(caseName);
				// classTr.add(rerunTimes);
				// }
				// } else if (caseIndex != 1) {
				// caseTr = new ArrayList<ReportCell>();
				// list.add(caseTr);
				// caseTr.add(caseName);
				// caseTr.add(rerunTimes);
				// }
				// }
				//
				// if (classIndex == 1 && catagoryIndex == 1) {
				// jobTr.add(failureCatagory);
				// } else if (classIndex != 1 && catagoryIndex == 1) {
				// if (classTr != null) {
				// classTr.add(failureCatagory);
				// }
				// } else if (catagoryIndex != 1) {
				// catagoryTr = new ArrayList<ReportCell>();
				// list.add(catagoryTr);
				// catagoryTr.add(failureCatagory);
				// }
				// }

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

	private void classResult2Report(RerunClassResult classResult) {
		List<ReportModel> list = classResult2ReportModel(classResult);
		reportModel2Report(list, reportDir + "\\" + classResult.getClassName()
				+ ".html");
	}

	private List<ReportModel> classResult2ReportModel(
			RerunClassResult classResult) {
		if (classResult != null) {
			List<ReportModel> reportModels = new ArrayList<ReportModel>();
			Map<String, List<TestCase>> failureCatagory = classResult
					.getFailureCatagory();
			ReportModel model = new ReportModel();

			List<String> headers = new ArrayList<String>();
			headers.add("TestCase");
			headers.add("Rerun Times");
			model.setHeaders(headers);
			List<List<ReportCell>> bodys = new ArrayList<List<ReportCell>>();
			for (Map.Entry<String, List<TestCase>> catagoryEntry : failureCatagory
					.entrySet()) {
				List<TestCase> caseList = catagoryEntry.getValue();
				for (TestCase testCase : caseList) {
					List<ReportCell> caseAndRerunTimesTr = new ArrayList<ReportCell>();
					ReportCell caseTd = new ReportCell("", "",
							testCase.getTestName());
					ReportCell rerunTimesTd = new ReportCell("", "", testCase
							.getResult().getRunCount() + "");
					caseAndRerunTimesTr.add(caseTd);
					caseAndRerunTimesTr.add(rerunTimesTd);
					bodys.add(caseAndRerunTimesTr);
				}
				String errorMsg = catagoryEntry.getKey();
				List<ReportCell> errorMsgTr = new ArrayList<ReportCell>();
				ReportCell errorMsgTd = new ReportCell("colspan=\"2\"", "",
						errorMsg);
				errorMsgTr.add(errorMsgTd);
				bodys.add(errorMsgTr);
			}
			model.setBodys(bodys);
			reportModels.add(model);
			return reportModels;
		}
		return null;
	}

	private void reportModel2Report(List<ReportModel> reportModelList,
			String reportOutputPath) {
		ReportFormatter formatReport = new ReportFormatter(reportModelList);
		String reportMessage = formatReport.formatReport();
		writeStringToFile(reportMessage, reportOutputPath);
	}

	private void writeStringToFile(String reportMessage, String reportOutputPath) {
		try {
			File reportFile = new File(reportOutputPath);
			if (!reportFile.exists()) {
				reportFile.createNewFile();
			}
			FileUtils.writeStringToFile(reportFile, reportMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintUtil.info("Success to generate report :" + reportOutputPath);
	}

}
