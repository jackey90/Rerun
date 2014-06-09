package com.ea.rerun.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ea.rerun.analyse.IAnalyse;
import com.ea.rerun.analyse.impl.AnalyseJenkinsTestResult;
import com.ea.rerun.analyse.model.MavenRerunTestCase;
import com.ea.rerun.common.util.PrintUtil;
import com.ea.rerun.feedback.IFeedBack;
import com.ea.rerun.feedback.impl.RerunFeedBack;
import com.ea.rerun.feedback.model.RerunJobResult;
import com.ea.rerun.getData.IGetData;
import com.ea.rerun.getData.impl.GetOrgData;
import com.ea.rerun.getData.model.orgData.JenkinsTestResult;

public class Rerun {
	private IGetData getRerunData;
	private IAnalyse analyseData;
	private static IFeedBack rerunFeedBack;
	public static int maxRerunTime;

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
		if (args.length == 1) {

			if (tryParseInt(args[0].toString())) {
				maxRerunTime = Integer.parseInt(args[0].toString());
				if (maxRerunTime <= 0 || maxRerunTime >= 10) {
					PrintUtil
							.error("the maxRerunTime should be ranged in [1,9]");
				}
				Rerun r = new Rerun();
				JenkinsTestResult result = r.getJenkinsTestResult();
				System.out.println(result.toString());
				AnalyseJenkinsTestResult an = new AnalyseJenkinsTestResult(
						result);
				List<MavenRerunTestCase> list = an.getAnalyseData();

				if (list.size() * maxRerunTime > 50) {
					// PrintUtil
					// .countDown(
					// "The rerun operation will take a long time, If you want to quit, please press CTRL + c",
					// 7);
					System.out
							.println("The rerun operation will take a long time, continue? Y/N");
					InputStreamReader is_reader = new InputStreamReader(
							System.in);
					try {
						String str = new BufferedReader(is_reader).readLine();
						if (!str.trim().toUpperCase().endsWith("Y")) {
							System.exit(0);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				r.rerunCommand(list);

				rerunFeedBack = new RerunFeedBack(r.formatFinalResult(list));
				rerunFeedBack.feedBack();
			} else {
				PrintUtil
						.error("The input param must be a integer! \neg: java -jar Rerun.jar 4");
			}
		} else if (args.length == 0) {
			PrintUtil
					.error("Please input the param: maxRerunNumber\neg: java -jar Rerun.jar 4");
		} else {
			PrintUtil.error("Invalid parameter\neg: java -jar Rerun.jar 4");
		}
	}

	private void rerunCommand(List<MavenRerunTestCase> list) {
		for (MavenRerunTestCase cmd : list) {
			cmd.getTestCase().run();
		}
	}

	private Map<String, Map<String, RerunJobResult>> formatFinalResult(
			List<MavenRerunTestCase> list) {
		Map<String, Map<String, RerunJobResult>> finalResult = new LinkedHashMap<String, Map<String, RerunJobResult>>();
		// Map<String, List<TestCase>> map = new HashMap<String,
		// List<TestCase>>();
		for (MavenRerunTestCase cmd : list) {
			// System.out.println(cmd.getTestCase().toString());
			// System.out.println(cmd.getTestCase().getMavenCommand());

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

		return finalResult;
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

	static boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
