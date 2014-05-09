package com.ea.rerun.getData.model.config;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;


public class RerunConfigFactory {
	private static Document doc;
	private final String configPath;
	private static RerunConfigFactory factory;
	

	private RerunConfigFactory(String configPath) {
		this.configPath = configPath;
		doc = getDocument();
	}

	public static RerunConfigFactory getInstance(String path) {
		if (factory == null) {
			factory = new RerunConfigFactory(path);
		}
		
		return factory;
	}

	private Document getDocument() {
		if (configPath != null) {
			SAXReader reader = new SAXReader();
			try {
				return reader.read(configPath);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		} else
			System.out.println("Error:  configPath is not initialized! ");

		return null;
	}

}
