package com.ea.rerun.common.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Node;

import com.ea.rerun.util.LogUtil;
import com.ea.rerun.util.MavenUtil;
import com.ea.rerun.util.PrintUtil;
import com.ea.rerun.util.XMLAnalyser;
import com.ibm.icu.math.BigDecimal;

public class TestResult {
	private int runCount;
	private List<TestFailure> failures;
	private List<TestSuccess> successes;
	private List<TestSkip> skips;
	private boolean shouldStop;
	private TestResultType resultType;
	private String errorDetails;
	private String errorStackTrace;
	private String stdout;
	private BigDecimal durationTime;

	public TestResult() {
		runCount = 0;
		failures = new ArrayList<TestFailure>();
		successes = new ArrayList<TestSuccess>();
		skips = new ArrayList<TestSkip>();
		shouldStop = false;
		resultType = TestResultType.UnStable_Failed;
		errorDetails = "";
		errorStackTrace = "";
		stdout = "";
		durationTime = BigDecimal.ZERO;
	}

	/**
	 * @author Jackey
	 * @date May 15, 2014
	 * @param test
	 * 
	 *            run the test, and add the result to the specific list
	 */
	public void run(final TestCase test) {
		while (!shouldStop()) {
			runCount++;
			PrintUtil.info(runCount + "times : " + test.toString());
			try {
				for (int i = -1; i >= 0; i--) {
					System.out.println("*********************   " + i
							+ " ***************************");
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//startRunCommand(test);
			getResult(test);
		}
	}

	private void startRunCommand(TestCase test) {
		MavenUtil.run(test.getMavenCommand());
	}

	public void copyReport(TestCase test, File report) {
		LogUtil.copyReport(test, report);
	}

	private void getResult(TestCase test) {
		String pomPath = test.getPomPath();
		if (pomPath != null) {
			String pomDirectory = pomPath.substring(0,
					pomPath.lastIndexOf("\\"));
			String juinitreportsPath = pomDirectory
					+ "\\target\\surefire-reports\\junitreports";
			File juniteReportDir = new File(juinitreportsPath);
			String reportName = "TEST-" + test.getPack() + "."
					+ test.getClassName() + ".xml";
			if (juniteReportDir.exists() && juniteReportDir.isDirectory()) {
				File[] reports = juniteReportDir.listFiles();
				for (File report : reports) {
					if (report.getName().equals(reportName)) {
						copyReport(test, report);
						analyseReport(report, test);
					}
				}
			}
		} else {
			PrintUtil.error("Can not find the pomPath!");
		}
	}

	@SuppressWarnings("unchecked")
	private void analyseReport(File report, Test test) {
		if (report != null) {
			XMLAnalyser reportAnalyser = new XMLAnalyser(report);
			List<Node> caseNodeList = reportAnalyser
					.getNodeList("//testsuite/testcase");
			if (caseNodeList != null && caseNodeList.size() > 0) {
				Node caseNode = caseNodeList.get(0);
				List<Node> childNodes = caseNode.selectNodes(".//*");
				if (childNodes != null && childNodes.size() > 0) {
					Node childNode = childNodes.get(0);
					String caseType = childNode.getName();
					if (caseType.equals("error") || caseType.equals("failure")) {
						addFailure(caseNode, test);
					} else {
						PrintUtil.warning("Unknow node: " + caseType);
					}
				} else {
					addSuccess(caseNode, test);
				}
			}
		}
	}

	private void addFailure(Node caseNode, Test test) {
		String durationTimeStr = caseNode.valueOf("@time");
		Node childNode = (Node) caseNode.selectNodes(".//*").get(0);
		String errorDetails = childNode.valueOf("@type");
		String errorStackTrace = childNode.getText();
		this.errorDetails = errorDetails;
		this.errorStackTrace = errorStackTrace;
		this.durationTime = new BigDecimal(durationTimeStr);
		TestFailure failure = new TestFailure(test, errorDetails,
				errorStackTrace, null, runCount,
				new BigDecimal(durationTimeStr));
		failures.add(failure);
	}

	private void addSuccess(Node caseNode, Test test) {
		String durationTimeStr = caseNode.valueOf("@time");
		this.durationTime = new BigDecimal(durationTimeStr);
		TestSuccess success = new TestSuccess(test, null, runCount,
				new BigDecimal(durationTimeStr));
		successes.add(success);
	}

	private void addSkip(Node caseNode, Test test) {

	}

	public boolean shouldStop() {
		for (TestSuccess success : successes) {
			if (success.getRunNumber() == runCount) {
				shouldStop = true;
				resultType = TestResultType.Successed;
				PrintUtil.info("Success !");
				return shouldStop;
			}
		}

		if (runCount <= 3) {

			if (failures.size() >= 2) {
				TestFailure lastFailure = failures.get(failures.size() - 1);
				if (lastFailure.getRunNumber() == runCount) {
					TestFailure preFailure = failures.get(failures.size() - 2);
					if (preFailure.getRunNumber() == runCount - 1) {
						if (preFailure.getErrorDetails() == null
								&& lastFailure.getErrorDetails() == null) {
							if (preFailure.getErrorStackTrace().equals(
									lastFailure.getErrorStackTrace())) {
								shouldStop = true;
								resultType = TestResultType.Stable_Failed;
								PrintUtil.info("Stable failure!");
							}
						} else if (preFailure.getErrorDetails() != null
								&& preFailure.getErrorDetails() != null) {
							if (preFailure.getErrorDetails().equals(
									preFailure.getErrorDetails())
									&& preFailure.getErrorStackTrace().equals(
											lastFailure.getErrorStackTrace())) {
								shouldStop = true;
								resultType = TestResultType.Stable_Failed;
								PrintUtil.info("Stable failure!");
							}
						}
					}
				}
			}

		}

		if (runCount >= 4) {
			shouldStop = true;
			resultType = TestResultType.UnStable_Failed;
			PrintUtil.info("Unstable failure");
		}

		return shouldStop;
	}

	public int getRunCount() {
		return runCount;
	}

	public List<TestFailure> getFailures() {
		return failures;
	}

	public List<TestSuccess> getSuccesses() {
		return successes;
	}

	public List<TestSkip> getSkips() {
		return skips;
	}

	public TestResultType getResultType() {
		return resultType;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public String getErrorStackTrace() {
		return errorStackTrace;
	}

	public String getStdout() {
		return stdout;
	}

	public BigDecimal getDurationTime() {
		return durationTime;
	}

}
