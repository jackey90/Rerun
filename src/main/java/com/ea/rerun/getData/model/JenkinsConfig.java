package com.ea.rerun.getData.model;

import java.util.List;
import java.util.Map;
import org.dom4j.Document;

public class JenkinsConfig {
	// singleton
	private static JenkinsConfig jenkinsConfig;
	private final Document doc;

	private final String url;
	private final String jenkinsFolder;
	private final Map<String, List<String>> views;

	private JenkinsConfig(Document doc) {
		this.doc = doc;
		url = getUrl();
		jenkinsFolder = getJenkinsFolder();
		views = getViews();
	}

	public static JenkinsConfig getInstance(Document doc) {
		if (jenkinsConfig == null) {
			jenkinsConfig = new JenkinsConfig(doc);
		}
		return jenkinsConfig;
	}

	private String getUrl() {
		return null;
	}

	private String getJenkinsFolder() {
		return null;
	}

	private Map<String, List<String>> getViews() {

		return null;
	}

}
