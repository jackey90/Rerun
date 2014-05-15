package com.ea.rerun.common.model;

import java.util.ArrayList;
import java.util.List;

import com.ea.rerun.analyse.model.MavenCommand;
import com.ea.rerun.util.MavenUtil;

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
	public void run(final TestCase test, MavenCommand command) {
		startRunCommand(test, command);
		runCount++;
		getResult(test, command);
	}

	private void startRunCommand(TestCase test, MavenCommand command) {
		MavenUtil.run(command.getCommand());
	}

	private void getResult(TestCase test, MavenCommand command) {
		
	}
	
	private void add
	
	private void 
	
}
