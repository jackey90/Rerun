package com.ea.rerun.common.model;

public class TestCase implements Test {
	private String branch;
	private String bundle;
	private String pack;
	private String className;
	private String testName;
	private String goals;
	private String pomPath;

	private TestResult result;

	public TestResult run() {
		result = getResult();
		run(result);
		return result;
	}

	public TestResult getResult() {
		if (result == null) {
			result = new TestResult();
		}
		return result;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBundle() {
		return bundle;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String toString() {
		return branch + ":" + bundle + ":" + pack + "." + className + "."
				+ testName;
	}

	public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	public String getPomPath() {
		return pomPath;
	}

	public void setPomPath(String pomPath) {
		this.pomPath = pomPath;
	}

	public String getMavenCommand() {
		return "mvn -B -f " + pomPath + " " + goals + " -Dtest=" + pack + "."
				+ className + "#" + testName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((bundle == null) ? 0 : bundle.hashCode());
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((pack == null) ? 0 : pack.hashCode());
		result = prime * result
				+ ((testName == null) ? 0 : testName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestCase other = (TestCase) obj;
		if (this.toString().equals(other.toString())) {
			return true;
		} else
			return false;
	}

	@Override
	public int countTestCases() {
		return 1;
	}

	@Override
	public void run(TestResult result) {
		result.run(this);
	}
	

}
