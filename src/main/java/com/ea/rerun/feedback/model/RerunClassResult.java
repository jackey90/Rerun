package com.ea.rerun.feedback.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ea.rerun.common.model.TestCase;

public class RerunClassResult {
	private String className;
	private Map<String, List<TestCase>> failureCatagory = new LinkedHashMap<String, List<TestCase>>();
	private List<TestCase> cases = new ArrayList<TestCase>();

	
	public RerunClassResult(String className){
		this.className = className;
	}
	
	public void addToCatagory(TestCase testCase) {
		cases.add(testCase);
		String category = testCase.getResult().getFailureSummary();
		if (failureCatagory.containsKey(category)) {
			List<TestCase> caseList = failureCatagory.get(category);
			if (caseList == null) {
				caseList = new ArrayList<TestCase>();
				failureCatagory.put(category, caseList);
			}
			caseList.add(testCase);
		} else {
			List<TestCase> caseList = new ArrayList<TestCase>();
			caseList.add(testCase);
			failureCatagory.put(category, caseList);
		}
	}
	
	public int getCount(){
		return cases.size();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, List<TestCase>> getFailureCatagory() {
		return failureCatagory;
	}

	public void setFailureCatagory(Map<String, List<TestCase>> failureCatagory) {
		this.failureCatagory = failureCatagory;
	}

	public List<TestCase> getCases() {
		return cases;
	}

	public void setCases(List<TestCase> cases) {
		this.cases = cases;
	}

}
