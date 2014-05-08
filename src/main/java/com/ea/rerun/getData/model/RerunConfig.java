package com.ea.rerun.getData.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
	// singleton
	private static RerunConfig config;
	// onece setted, cannot be changed
	private final String configPath;
	private final Document doc;

	private final JenkinsConfig jenkinsConfig;

	private RerunConfig() {
		configPath = "rerunConfig.xml";
		doc = getDocument();
		jenkinsConfig = JenkinsConfig.getInstance(doc);
	}

	private RerunConfig(String path) {
		this.configPath = path;
		doc = getDocument();
		jenkinsConfig = JenkinsConfig.getInstance(doc);
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

	public Document getDocument() {
		try {
			InputStream inputStream = new FileInputStream(new File(configPath));
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(inputStream);
			return document;
		} catch (IOException e) {
			
		} catch (DocumentException e) {

		}

		return null;

	}

}
