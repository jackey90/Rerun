package com.ea.rerun.getData.model.config;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;

public abstract class AbstractConfig {
	protected final Document doc;

	protected AbstractConfig(Document doc) {
		this.doc = doc;
	}

	protected List<String> getNodeStringList(String nodePath) {
		if (doc != null) {
			List nodeList = doc.selectNodes(nodePath);
			if (nodeList != null && nodeList.size() > 0) {
				List<String> list = new ArrayList<String>();
				for (Object node : nodeList) {
					if (((Node) node).getText() != null) {
						list.add(((Node) node).getText());
					}
				}

				return list;

			}
		}

		return null;
	}

	protected String getNodeString(String nodePath) {
		if (doc != null) {
			Node node = doc.selectSingleNode(nodePath);
			if (node != null) {
				return node.getText();
			}
		}
		return null;
	}

	public void warning(String str) {
		System.out.println("warning ***********************");
		System.out.println(str);
		System.out.println();
	}

	public void error(String str) {
		System.out.println("error xxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println(str);
		System.out.println();
	}

}
