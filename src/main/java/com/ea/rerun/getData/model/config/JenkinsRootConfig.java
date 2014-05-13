package com.ea.rerun.getData.model.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.ea.rerun.util.PrintUtil;

public class JenkinsRootConfig {

	// singleton
	private static JenkinsRootConfig jenkinsConfig;
	private final String jenkinsPath;
	private final Map<String, List<String>> views;
	private final Map<String, String> globalNodeProperties;

	private final Document doc;

	private JenkinsRootConfig(String path) {
		jenkinsPath = path;
		doc = getDocument();
		views = genViews();
		globalNodeProperties = genGlobalNodeProperties();
	}

	public static JenkinsRootConfig getInstance(String path) {
		if (jenkinsConfig == null) {
			jenkinsConfig = new JenkinsRootConfig(path);
		}

		return jenkinsConfig;
	}

	public Map<String, List<String>> getViews() {
		return views;
	}

	public Map<String, String> getGlobalNodeProperties() {
		return globalNodeProperties;
	}

	@SuppressWarnings("unchecked")
	private Map<String, List<String>> genViews() {
		if (doc != null) {
			List<Node> viewNodeList = doc
					.selectNodes("//hudson/views/listView");
			if (viewNodeList != null && viewNodeList.size() > 0) {
				Map<String, List<String>> result = new HashMap<String, List<String>>();

				for (Node viewNode : viewNodeList) {
					Node nameNode = viewNode.selectSingleNode("name");
					if (nameNode != null) {
						List<Node> jobNamesStringNodes = viewNode
								.selectNodes("jobNames/string");
						if (jobNamesStringNodes != null
								&& jobNamesStringNodes.size() > 0) {
							List<String> stringList = new ArrayList<String>();
							for (Node stringNode : jobNamesStringNodes) {
								stringList.add(stringNode.getText());
							}
							result.put(nameNode.getText(), stringList);
						}
					}
				}
				return result;
			} else {
				PrintUtil.warning("there is no listViews!");
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> genGlobalNodeProperties() {
		if (doc != null) {
			List<Node> viewNodeList = doc
					.selectNodes("//hudson/globalNodeProperties/hudson.slaves.EnvironmentVariablesNodeProperty/envVars/tree-map/string");
			if (viewNodeList != null && viewNodeList.size() > 0
					&& viewNodeList.size() % 2 == 0) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < viewNodeList.size() - 1; i = i + 2) {
					if (!isNullOrEmpty(viewNodeList.get(i).getText())
							&& !isNullOrEmpty(viewNodeList.get(i + 1).getText())) {
						map.put(viewNodeList.get(i).getText(), viewNodeList
								.get(i + 1).getText());
					}
				}
				
				return map;
			}
		}
		return null;
	}

	private Document getDocument() {
		SAXReader reader = new SAXReader();
		try {
			if (!new File(jenkinsPath).exists()) {

				String temp = "C:\\Program Files (x86)\\Jenkins\\config.xml";
				if (new File(temp).exists()) {
					PrintUtil.warning(jenkinsPath
							+ "  is not found, using default: " + temp);
					return reader.read(temp);
				} else {
					PrintUtil.error("can not find the jenkinsPath");
					return null;
				}
			}

			return reader.read(jenkinsPath);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean isNullOrEmpty(String str) {
		if (str == null || str.length() <= 0) {
			return true;
		}

		return false;
	}

}
