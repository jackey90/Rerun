package com.ea.rerun.getData.model.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.ea.rerun.util.PrintUtil;

/**
 * @author Jackey jaceky90.hj@gmail.com
 * @Date May 7, 2014
 * 
 *       translated from the rerunConfig.xml
 */
public class RerunConfig {
	public static final String currentPath = System.getProperty("user.dir");
	// singleton
	private static RerunConfig config;
	// onece setted, cannot be changed
	private final String configPath;
	private static Document doc;

	private final RerunJenkinsConfig jenkinsConfig;
	private final RerunReportConfig reportConfig;
	private final RerunLogConfig logConfig;
	private final RerunMailConfig mailConfig;
	
	public RerunJenkinsConfig getJenkinsConfig() {
		return jenkinsConfig;
	}

	public RerunReportConfig getReportConfig() {
		return reportConfig;
	}

	public RerunLogConfig getLogConfig() {
		return logConfig;
	}

	public RerunMailConfig getMailConfig() {
		return mailConfig;
	}

	private RerunConfig() {
		configPath = currentPath + "\\rerunConfig.xml";
		doc = getDocument();
		jenkinsConfig = RerunJenkinsConfig.getInstance(doc);
		reportConfig = RerunReportConfig.getInstance(doc);
		logConfig = RerunLogConfig.getInstance(doc);
		mailConfig = RerunMailConfig.getInstance(doc);
	}

	private RerunConfig(String path) {
		this.configPath = path;
		doc = getDocument();
		jenkinsConfig = RerunJenkinsConfig.getInstance(doc);
		reportConfig = RerunReportConfig.getInstance(doc);
		logConfig = RerunLogConfig.getInstance(doc);
		mailConfig = RerunMailConfig.getInstance(doc);
	}

	public static RerunConfig getInstance() {
		if (config == null) {
			config = new RerunConfig();
		}
		return config;
	}

	public static RerunConfig getInstance(String configPath) {
		if (config == null) {
			config = new RerunConfig(configPath);
		}
		return config;
	}

	private Document getDocument() {
		SAXReader reader = new SAXReader();
		if (configPath != null) {
			try {
				return reader.read(configPath);
			} catch (DocumentException e) {
				try {
					PrintUtil.warning("rerunConfig.xml is not found, use default!");
					return reader
							.read("src\\main\\resources\\defaultRerunConfig.xml");
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			PrintUtil.warning("rerunConfig.xml is not found, use default!");
			try {
				return reader
						.read("src\\main\\resources\\defaultRerunConfig.xml");
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
