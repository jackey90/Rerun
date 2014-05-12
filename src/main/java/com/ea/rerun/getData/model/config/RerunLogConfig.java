package com.ea.rerun.getData.model.config;

import org.dom4j.Document;

import com.ea.rerun.util.XMLAnalyser;

public class RerunLogConfig extends XMLAnalyser {

	// singleton
	private static RerunLogConfig logConfig;

	private final KeepLogEnum keepLogEnum;
	private final int daysToKeep;
	private final String logPath;

	private RerunLogConfig(Document doc) {
		super(doc);
		keepLogEnum = generateKeepLogEnum();
		daysToKeep = generateDaysToKeep();
		logPath = generateLogPath();
	}

	public static RerunLogConfig getInstance(Document doc) {
		if (logConfig == null) {
			logConfig = new RerunLogConfig(doc);
		}
		return logConfig;
	}

	private KeepLogEnum generateKeepLogEnum() {
		String result = getNodeString("/rerunConfig/log/keepLog");
		if (isNullOrEmpty(result)) {
			warning("keepLog not set, use default: " + KeepLogEnum.KeepFailure);
			return KeepLogEnum.KeepFailure;
		}
		return KeepLogEnum.valueOf(result);
	}

	private int generateDaysToKeep() {
		String result = getNodeString("/rerunConfig/log/daysToKeepLog");
		if (isNullOrEmpty(result)) {
			warning("daysToKeepLog not set, use default : 50 days");
			return 50;
		}
		return Integer.parseInt(result.trim());
	}

	private String generateLogPath() {
		String result = getNodeString("/rerunConfig/log/logPath");
		if (isNullOrEmpty(result)) {
			result = RerunConfig.currentPath + "\\rerunLog";
			warning("logPath not set, use default :  " + result);
		}
		return null;
	}

	public KeepLogEnum getKeepLogEnum() {
		return keepLogEnum;
	}

	public int getDaysToKeep() {
		return daysToKeep;
	}

	public String getLogPath() {
		return logPath;
	}

}
