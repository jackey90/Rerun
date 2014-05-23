package com.ea.rerun.main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ea.rerun.analyse.IAnalyse;
import com.ea.rerun.analyse.impl.AnalyseJenkinsTestResult;
import com.ea.rerun.analyse.model.MavenRerunTestCase;
import com.ea.rerun.common.model.TestCase;
import com.ea.rerun.feedback.IFeedBack;
import com.ea.rerun.feedback.impl.RerunFeedBack;
import com.ea.rerun.feedback.model.RerunClassResult;
import com.ea.rerun.feedback.model.RerunJobResult;
import com.ea.rerun.getData.IGetData;
import com.ea.rerun.getData.impl.GetOrgData;
import com.ea.rerun.getData.model.orgData.JenkinsTestResult;

public class Rerun {
	private IGetData getRerunData;
	private IAnalyse analyseData;
	private static IFeedBack rerunFeedBack;

	public Rerun() {
		getRerunData = new GetOrgData();
		analyseData = new AnalyseJenkinsTestResult(getJenkinsTestResult());
	}

	public void setAnalyseData(IAnalyse analyseData) {
		this.analyseData = analyseData;

	}

	public JenkinsTestResult getJenkinsTestResult() {
		return (JenkinsTestResult) getRerunData.getData();
	}

	public static void main(String[] args) {
		Rerun r = new Rerun();
		JenkinsTestResult result = r.getJenkinsTestResult();
		AnalyseJenkinsTestResult an = new AnalyseJenkinsTestResult(result);
		List<MavenRerunTestCase> list = an.getAnalyseData();
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getTestCase().toString());
		}
		List<String> strList = new ArrayList<String>();
		int i = 1;
		Map<String, Map<String, RerunJobResult>> finalResult = new LinkedHashMap<String, Map<String, RerunJobResult>>();
		// Map<String, List<TestCase>> map = new HashMap<String,
		// List<TestCase>>();
		for (MavenRerunTestCase cmd : list) {
			System.out.println(cmd.getTestCase().toString());
			System.out.println(cmd.getTestCase().getMavenCommand());
			cmd.getTestCase().run();

			if (finalResult.containsKey(cmd.getViewName())) {
				Map<String, RerunJobResult> map = finalResult.get(cmd
						.getViewName());
				if (map == null) {
					map = new LinkedHashMap<String, RerunJobResult>();
				}
				if (map.containsKey(cmd.getJobName())) {
					RerunJobResult jobResult = map.get(cmd.getJobName());
					if (jobResult == null) {
						jobResult = new RerunJobResult(cmd.getViewName(),
								cmd.getJobName());

					}
					jobResult.addTestCase(cmd.getTestCase());

				} else {
					RerunJobResult jobResult = new RerunJobResult(
							cmd.getViewName(), cmd.getJobName());
					jobResult.addTestCase(cmd.getTestCase());
					map.put(cmd.getJobName(), jobResult);
				}
			} else {
				Map<String, RerunJobResult> map = new LinkedHashMap<String, RerunJobResult>();
				RerunJobResult jobResult = new RerunJobResult(
						cmd.getViewName(), cmd.getJobName());
				jobResult.addTestCase(cmd.getTestCase());
				map.put(cmd.getJobName(), jobResult);
				finalResult.put(cmd.getViewName(), map);
			}
		}

		for (Map.Entry<String, Map<String, RerunJobResult>> finalEntry : finalResult
				.entrySet()) {
			System.out.println("**********************  " + finalEntry.getKey()
					+ "  *****************");
			for (Map.Entry<String, RerunJobResult> jobEntry : finalEntry
					.getValue().entrySet()) {
				RerunJobResult jobResult = jobEntry.getValue();
				System.out.println("job    "
						+ jobResult.getJobName());
				for (Map.Entry<String, RerunClassResult> classEntry : jobResult
						.getClassResults().entrySet()) {
					System.out.println("Class    "
							+ classEntry.getKey());
					for (Map.Entry<String, List<TestCase>> caseEntry : classEntry
							.getValue().getFailureCatagory().entrySet()) {
						System.out.println("failureCatagory       " +caseEntry.getKey());
						for (TestCase testCase : caseEntry.getValue()) {
							System.out
									.println("testCase    "
											+ testCase.toString());
						}
					}
				}

			}
			System.out.println();
		}
		rerunFeedBack = new RerunFeedBack(finalResult);

		rerunFeedBack.feedBack();

		System.out.println();
	}

	public IGetData getGetRerunData() {
		return getRerunData;
	}

	public void setGetRerunData(IGetData getRerunData) {
		this.getRerunData = getRerunData;
	}

	public IFeedBack getRerunFeedBack() {
		return rerunFeedBack;
	}

	public void setRerunFeedBack(IFeedBack rerunFeedBack) {
		this.rerunFeedBack = rerunFeedBack;
	}

	public IAnalyse getAnalyseData() {
		return analyseData;
	}

}
