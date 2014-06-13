package com.ea.rerun.getData.model.config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.ea.rerun.common.util.PrintUtil;

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
		configPath = currentPath + "\\RerunConfig.xml";
		// System.out.println("configPath================  " + configPath);
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
			try {
				return reader.read(configPath);
			} catch (DocumentException e) {
				if(generateRerunConfig()){
					System.out.println("RerunConfig.xml generated, and jenkinsFolder is set to %ProgramFiles(x86)%\\Jenkins by default");
				}else{
					System.out.println("RerunConfig.xml can't be generated!");
					e.printStackTrace();
				}
				System.exit(0);
				return null;
			}
	}

	private boolean generateRerunConfig() {
		try {
			File rerurnConfig = new File(currentPath + "\\RerunConfig.xml");
			FileUtils.copyInputStreamToFile(this.getClass()
					.getResourceAsStream("/resources/defaultRerunConfig.xml"),
					rerurnConfig);
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
			return false;
		}

	}
}
