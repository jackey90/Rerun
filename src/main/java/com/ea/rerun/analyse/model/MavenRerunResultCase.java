package com.ea.rerun.analyse.model;

import com.ea.rerun.common.model.TestCase;
import com.ibm.icu.math.BigDecimal;

/**
 * @author Jackey
 * @Date May 8, 2014
 * 
 *       maven's junit result, the files are stored under the surefire-reports
 *       folders
 * 
 *       eg:nucleus\NNG\billing-integration\target\surefire-reports
 *       \junitreports\TEST-com.ea.nucleus.billing.account.banktransfer.
 *       TestPutBankTransferAccount.xml
 */
public class MavenRerunResultCase {
	private TestCase testCase;
	private RerunResultCaseStatusEnun status;
	private BigDecimal time;
	private String errorDetails;
	private String errorStackTrace;
	
	public TestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}

	public RerunResultCaseStatusEnun getStatus() {
		return status;
	}

	public void setStatus(RerunResultCaseStatusEnun status) {
		this.status = status;
	}

	public BigDecimal getTime() {
		return time;
	}

	public void setTime(BigDecimal time) {
		this.time = time;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getErrorStackTrace() {
		return errorStackTrace;
	}

	public void setErrorStackTrace(String errorStackTrace) {
		this.errorStackTrace = errorStackTrace;
	}

}
