package com.ea.rerun.getData.model.config;

import org.dom4j.Document;

public class LogConfig extends AbstractConfig {

	// singleton
	private static LogConfig logConfig;

	private final KeepLogEnum keepLogEnum;
	private final int daysToKeep;
	private final String logPath;

	private LogConfig(Document doc) {
		super(doc);
		keepLogEnum = generateKeepLogEnum();
		daysToKeep = generateDaysToKeep();
		logPath = generateLogPath();
	}

	public static LogConfig getInstance(Document doc) {
		if (logConfig == null) {
			logConfig = new LogConfig(doc);
		}
		return logConfig;
	}

	private KeepLogEnum generateKeepLogEnum() {
		String result = getNodeString("/rerunConfig/log/keepLog");
		if (result == null) {
			warning("keepLog not set, use default" + KeepLogEnum.KeepFailure);
			return KeepLogEnum.KeepFailure;
		}
		return KeepLogEnum.valueOf(result);
	}

	private int generateDaysToKeep() {
		String result = getNodeString("/rerunConfig/log/daysToKeepLog");
		if (result == null) {
			warning("daysToKeepLog not set, use default : 50 days");
			return 50;
		}
		return Integer.parseInt(result.trim());
	}

	private String generateLogPath() {
		String result = getNodeString("/rerunConfig/log/logPath");
		if (result == null) {
			result = RerunConfig.currentPath + "\\rerunLog";
			warning("logPath not set, use default : " + result);
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
