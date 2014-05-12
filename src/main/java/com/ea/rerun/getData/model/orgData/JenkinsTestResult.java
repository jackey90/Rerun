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
 *       -integration\builds\36
 */
public class JenkinsTestResult {
	private Map<String, List<JenkinsJob>> views;

	public Map<String, List<JenkinsJob>> getViews() {
		return views;
	}

	public void setViews(Map<String, List<JenkinsJob>> views) {
		this.views = views;
	}

}
