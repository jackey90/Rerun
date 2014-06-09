package com.ea.rerun.getData.model.orgData;

import java.util.List;
import java.util.Map;

/**
 * @author Jackey jaceky90.hj@gmail.com
 * @Date May 8, 2014
 * 
 *       the jenkins test result, stored under jenkins' builds folder, eg:
 *       Jenkins
 *       \.jenkins\jobs\billing-Comprehensive\modules\com.ea.nucleus$billing
 *       -integration\builds\36\junitResult.xml
 */
public class JenkinsTestResult {
	private Map<String, List<JenkinsJob>> views;

	public Map<String, List<JenkinsJob>> getViews() {
		return views;
	}

	public void setViews(Map<String, List<JenkinsJob>> views) {
		this.views = views;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		String viewName;
		List<JenkinsJob> jobList;
		for (Map.Entry<String, List<JenkinsJob>> viewEntry : views.entrySet()) {
			viewName = viewEntry.getKey();
			jobList = viewEntry.getValue();
			sb.append("****************************  " + viewName
					+ " ****************************\n");
			for (JenkinsJob job : jobList) {
				String jobStr = job.toString();
				if (!jobStr.equals("")) {
					sb.append(jobStr);
				}
			}
		}
		return sb.toString();
	}

}
