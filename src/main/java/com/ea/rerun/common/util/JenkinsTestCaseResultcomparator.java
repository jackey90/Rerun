package com.ea.rerun.common.util;

import java.util.Comparator;

import com.ea.rerun.getData.model.orgData.JenkinsJunitResultCase;
import com.ibm.icu.math.BigDecimal;

public class JenkinsTestCaseResultcomparator implements
		Comparator<JenkinsJunitResultCase> {
	private final BigDecimal errorStackTraceComparePercentage;

	public JenkinsTestCaseResultcomparator() {
		this.errorStackTraceComparePercentage = new BigDecimal(0.5);
	}

	public JenkinsTestCaseResultcomparator(BigDecimal per) {
		if (per != null && per.compareTo(new BigDecimal(0)) >= 0
				&& per.compareTo(new BigDecimal(1)) <= 0) {
			this.errorStackTraceComparePercentage = per;
		} else {
			this.errorStackTraceComparePercentage = new BigDecimal(0.5);
		}
	}

	public int compare(JenkinsJunitResultCase o1, JenkinsJunitResultCase o2) {
		String errorStackTrace1 = o1.getErrorStackTrace();
		String errorStackTrace2 = o2.getErrorStackTrace();
		if (o1.getErrorDetails().equals(o2.getErrorDetails())) {
			int length = errorStackTrace1.length();
			BigDecimal compareBLength = errorStackTraceComparePercentage
					.multiply(new BigDecimal(length));
			int compareLength = compareBLength.intValue();
			if (errorStackTrace2.length() >= compareLength) {
				for (int i = 0; i < compareLength; i++) {
					if (errorStackTrace1.charAt(i) != errorStackTrace2
							.charAt(i)) {
						return -1;
					}
				}
				return 0;
			}
		}
		return -1;
	}

}
