package com.ea.rerun.feedback.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

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
 * @author Jackey
 * @Date May 8, 2014
 * 
 *       give the feedback: generate the result.html
 */
public class RerunFeedBack implements IFeedBack {
	Map<String, Map<String, RerunJobResult>> finalResult;
	private String logPath;
	private String reportDirPath;
	private String classReportDirPath;

	public RerunFeedBack(Map<String, Map<String, RerunJobResult>> finalResult) {
		// InetAddress addr;
		// try {
		// addr = InetAddress.getLocalHost();
		// String address = addr.getHostAddress();
		// url = "<a" + "http://" + address + ":8080" + ">" + "http://"
		// + address + ":8080" + "</a>";
		// } catch (UnknownHostException e) {
		// e.printStackTrace();
		// }
		String reportPath = RerunConfig.getInstance().getReportConfig()
				.getReportOutPutPath();
		reportDirPath = reportPath.substring(0, reportPath.lastIndexOf("\\"));
		File classReportDir = new File(reportDirPath + "\\classReport");
		if (!classReportDir.exists()) {
			classReportDir.mkdir();
		}
		classReportDirPath = classReportDir.getAbsolutePath();
		logPath = RerunConfig.getInstance().getLogConfig().getLogPath();
		this.finalResult = finalResult;
	}

	public void feedBack() {

		List<ReportModel> classReport = toReportModel(finalResult);
		reportModel2Report(classReport, classReportDirPath + "\\class.html",
				false);

		File result = new File(reportDirPath + "\\Result.html");
		try {
			FileUtils.copyInputStreamToFile(this.getClass()
					.getResourceAsStream("/resources/Result.html"), result);

			File tempHtml = new File(classReportDirPath + "\\welcome.html");
			FileUtils.copyInputStreamToFile(this.getClass()
					.getResourceAsStream("/resources/welcome.html"), tempHtml);
		} catch (IOException e) {
			e.printStackTrace();
		}

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
				System.out.println();
				System.out
						.println("Folder rerunLog and following files generated:");
				// Map<String, String> fields = new LinkedHashMap<String,
				// String>();
				// fields.put("Title : ", viewName);
				// fields.put("URL : ", url);
				// fields.put("Report Date: ", new Date().toString());
				// reportModel.setFields(fields);
				List<String> headers = getHeaders(false);
				reportModel.setHeaders(headers);
				List<List<ReportCell>> bodys = new ArrayList<List<ReportCell>>();

				// those who rerun failed
				for (Map.Entry<String, RerunJobResult> jobResultEntry : jobResultMap
						.entrySet()) {
					if (!jobResultEntry.getKey().equals("Success")) {
						List<List<ReportCell>> jobTrs = getTr(
								jobResultEntry.getValue(), false);
						bodys.addAll(jobTrs);
					}
				}
				// those who rerun succeed
				for (Map.Entry<String, RerunJobResult> jobResultEntry : jobResultMap
						.entrySet()) {
					if (jobResultEntry.getKey().equals("Success")) {
						List<List<ReportCell>> jobTrs = getTr(
								jobResultEntry.getValue(), false);
						bodys.addAll(jobTrs);
					}
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
			Map<String, RerunClassResult> classResultMap = jobResult
					.getClassResults();
			List<ReportCell> jobTr = new ArrayList<ReportCell>();
			list.add(jobTr);
			ReportCell jobTd = new ReportCell("rowspan=\""
					+ classResultMap.size() + "\"", "", jobResult.getJobName());
			jobTr.add(jobTd);

			// Iterator<RerunClassResult> classResultItr =
			// classResultMap.values().iterator();
			// RerunClassResult firestClassResult = classResultItr.next();
			int classIndex = 0;
			for (Map.Entry<String, RerunClassResult> classResultEntry : classResultMap
					.entrySet()) {
				classIndex++;
				List<ReportCell> classTr = null;
				RerunClassResult classResult = classResultEntry.getValue();

				ReportCell classNameTd = new ReportCell(
						"rowspan=\"" + 1 + "\"", "href=\""
								+ classResult.getClassName() + ".html"
								+ "\" target=\"details\"",
						classResult.getClassName());
				if (classIndex == 1) {
					jobTr.add(classNameTd);
				} else {
					classTr = new ArrayList<ReportCell>();
					list.add(classTr);
					classTr.add(classNameTd);
				}

				classResult2Report(classResult, true);

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

	private void classResult2Report(RerunClassResult classResult,
			boolean needPrint) {
		List<ReportModel> list = classResult2ReportModel(classResult);
		reportModel2Report(list,
				classReportDirPath + "\\" + classResult.getClassName()
						+ ".html", needPrint);
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
			headers.add("Status");
			headers.add("Rerun Times");
			model.setHeaders(headers);
			List<List<ReportCell>> bodys = new ArrayList<List<ReportCell>>();
			for (Map.Entry<String, List<TestCase>> catagoryEntry : failureCatagory
					.entrySet()) {
				List<TestCase> caseList = catagoryEntry.getValue();
				for (TestCase testCase : caseList) {
					List<ReportCell> caseAndRerunTimesTr = new ArrayList<ReportCell>();
					ReportCell caseTd = new ReportCell("", "",
							testCase.getPack() + "." + testCase.getClassName()
									+ "#" + testCase.getTestName());

					String status = "Unknown";
					switch (testCase.getResult().getResultType()) {
					case Error:
						status = "Error";
						break;
					case Successed:
						status = "Success";
						break;
					case Skiped:
						status = "Skiped";
						break;
					default:
						status = "Failed";
						break;
					}
					ReportCell statusTd = new ReportCell("",
							"style=\"font-weight:bold\"", status);

					ReportCell rerunTimesTd = new ReportCell("", "", testCase
							.getResult().getRunCount() + "");
					caseAndRerunTimesTr.add(caseTd);
					caseAndRerunTimesTr.add(statusTd);
					caseAndRerunTimesTr.add(rerunTimesTd);
					bodys.add(caseAndRerunTimesTr);
				}
				String errorMsg = catagoryEntry.getKey();
				List<ReportCell> errorMsgTr = new ArrayList<ReportCell>();
				ReportCell errorMsgTd = new ReportCell("colspan=\""
						+ headers.size() + "\"", "", errorMsg);
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
			String reportOutputPath, boolean needPrint) {
		ReportFormatter formatReport = new ReportFormatter(reportModelList);
		String reportMessage = formatReport.formatReport();
		writeStringToFile(reportMessage, reportOutputPath, needPrint);
	}

	private void writeStringToFile(String reportMessage,
			String reportOutputPath, boolean needPrint) {
		try {
			File reportFile = new File(reportOutputPath);
			if (!reportFile.exists()) {
				reportFile.createNewFile();
			}
			FileUtils.writeStringToFile(reportFile, reportMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (needPrint) {
			String str = reportOutputPath.substring(
					reportOutputPath.lastIndexOf("\\") + 1,
					reportOutputPath.length());
			System.out.println("classReport\\" + str);
		}
	}
}
