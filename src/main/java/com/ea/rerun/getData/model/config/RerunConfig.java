package com.ea.rerun.getData.model.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

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

	private final JenkinsConfig jenkinsConfig;
	private final ReportConfig reportConfig;
	private final LogConfig logConfig;
	private final MailConfig mailConfig;

	private RerunConfig() {
		configPath = currentPath + "\\rerunConfig.xml";
		doc = getDocument();
		jenkinsConfig = JenkinsConfig.getInstance(doc);
		reportConfig = ReportConfig.getInstance(doc);
		logConfig = LogConfig.getInstance(doc);
		mailConfig = MailConfig.getInstance(doc);
	}

	private RerunConfig(String path) {
		this.configPath = path;
		doc = getDocument();
		jenkinsConfig = JenkinsConfig.getInstance(doc);
		reportConfig = ReportConfig.getInstance(doc);
		logConfig = LogConfig.getInstance(doc);
		mailConfig = MailConfig.getInstance(doc);
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
					System.out.println("Warning:  rerunConfig.xml is not found, use default!");
					return reader.read("defaultRerunConfig.xml");
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			System.out
					.println("Warning:  rerunConfig.xml is not found, use default! ");
			try {
				return reader.read("defaultRerunConfig.xml");
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
