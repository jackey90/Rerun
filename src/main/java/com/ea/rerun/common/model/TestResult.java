package com.ea.rerun.common.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

import com.ea.rerun.util.MavenUtil;
import com.ea.rerun.util.PrintUtil;
import com.ea.rerun.util.XMLAnalyser;

public class TestResult {
	private int runCount;
	private List<TestFailure> failures;
	private List<TestSuccess> success;
	private List<TestSkip> skips;
	private boolean shouldStop;

	public TestResult() {
		runCount = 0;
		failures = new ArrayList<TestFailure>();
		success = new ArrayList<TestSuccess>();
		skips = new ArrayList<TestSkip>();
		shouldStop = false;
	}

	/**
	 * @author Jackey
	 * @date May 15, 2014
	 * @param test
	 * 
	 *            run the test, and add the result to the specific list
	 */
	public void run(final TestCase test) {
		startRunCommand(test);
		runCount++;
		getResult(test);
	}

	private void startRunCommand(TestCase test) {
		MavenUtil.run(test.getMavenCommand());
	}

	private void getResult(TestCase test) {
		String pomPath = test.getPomPath();
		if (pomPath != null) {
			String pomDirectory = pomPath
					.substring(0, pomPath.lastIndexOf("."));
			String juinitreportsPath = pomDirectory
					+ "\\target\\surefire-reports\\junitreports";
			File juniteReportDir = new File(juinitreportsPath);
			String reportName = "TEST-" + test.getPack() + "."
					+ test.getClassName() + "." + test.getTestName() + ".xml";
			if (juniteReportDir.exists() && juniteReportDir.isDirectory()) {
				File[] reports = juniteReportDir.listFiles();
				for (File report : reports) {
					if (report.getName().equals(reportName)) {
						analyseReport(report);
					}
				}
			}
		} else {
			PrintUtil.error("Can not find the pomPath!");
		}
	}

	private void analyseReport(File report) {
		if (report != null) {
			XMLAnalyser reportAnalyser = new XMLAnalyser(report);
			List<Node> caseNodeList = reportAnalyser
					.getNodeList("//testsuite/testcase");
			if (caseNodeList != null && caseNodeList.size() > 0) {
				Node caseNode = caseNodeList.get(0);
				List<Node> childNodes = caseNode.selectNodes("//*");
				if(childNodes != null && childNodes.size() > 0){
				Node childNode = childNodes.get(0);
				String caseType = childNode.getName();
				if (caseType.equals("error") || caseType.equals("failure")) {
					addFailure(caseNode);
				} else {
					addSuccess(caseNode);
				}
				}
			}
		}
	}

	private void addFailure(Node caseNode) {

	}

	private void addSuccess(Node caseNode) {
	}

	private void addSkip(Node caseNode) {

	}

}
