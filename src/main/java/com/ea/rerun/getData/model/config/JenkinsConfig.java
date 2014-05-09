package com.ea.rerun.getData.model.config;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class JenkinsConfig extends AbstractConfig {
	// singleton
	private static JenkinsConfig jenkinsConfig;

	private final String url;
	private final String jenkinsFolder;
	private final Map<String, List<String>> views;

	private JenkinsConfig(Document doc) {
		super(doc);
		url = generateUrl();
		jenkinsFolder = generateJenkinsFolder();
		views = generateViews();
	}

	public static JenkinsConfig getInstance(Document doc) {
		if (jenkinsConfig == null) {
			jenkinsConfig = new JenkinsConfig(doc);
		}
		return jenkinsConfig;
	}

	@SuppressWarnings("unused")
	private String generateUrl() {
		String result = getNodeString("//rerunConfig/jenkinsConfig/jenkinsURL");
		if (result == null) {
			String address = null;
			InetAddress addr;
			try {
				addr = InetAddress.getLocalHost();
				address = addr.getHostName().toString();
				warning("JenKinsURL not set, use default :" + address);
				return address + ":8080";
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private String generateJenkinsFolder() {
		String result = getNodeString("//rerunConfig/jenkinsConfig/jenkinsFolder");
		if (result == null) {
			result = "C:\\Program Files (x86)\\Jenkins";
			if (new File(result).exists()) {
				warning("jenkinsFolder not set, use default:" + result);
				return result;
			} else {
				error("jenkinsFolder not set!");
			}
		}
		return result;
	}

	private Map<String, List<String>> generateViews() {
		if (doc != null) {
			List<Node> viewNodeList = doc
					.selectNodes("//rerunConfig/jenkinsConfig/views/listView");
			if (viewNodeList != null && viewNodeList.size() > 0) {
				Map<String, List<String>> result = new HashMap<String, List<String>>();

				for (Node viewNode : viewNodeList) {
					Node nameNode = viewNode.selectSingleNode("name");
					if (nameNode != null) {
						List<Node> jobNamesStringNodes = viewNode
								.selectNodes("//jobNames/string");
						if (jobNamesStringNodes != null
								&& jobNamesStringNodes.size() > 0) {
							List<String> stringList = new ArrayList<String>();
							for (Node stringNode : jobNamesStringNodes) {
								stringList.add(stringNode.getText());
							}
							result.put(nameNode.getText(), stringList);
						} else
							result.put(nameNode.getText(), null);
					}
				}

				return result;
			} else {
				warning("listView not set, will scan all views.");
			}
		}
		return null;
	}

	public String getUrl() {
		return url;
	}

	public String getJenkinsFolder() {
		return jenkinsFolder;
	}

	public Map<String, List<String>> getViews() {
		return views;
	}

}
