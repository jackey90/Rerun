package com.ea.rerun.getData.model.config;

import java.util.List;

import org.dom4j.Document;

import com.ea.rerun.common.util.XMLAnalyser;

public class RerunMailConfig extends XMLAnalyser {

	// singleton
	private static RerunMailConfig mailConfig;
	private final boolean enable;
	private final String sTMPHost;
	private final String subject;
	private final String from;
	private final List<String> toList;
	private final List<String> ccList;

	private RerunMailConfig(Document doc) {
		super(doc);
		enable = generateEnable();
		sTMPHost = generateSTMPHost();
		subject = generateSubject();
		from = generateFrom();
		toList = generateToList();
		ccList = generateCCList();
	}

	public static RerunMailConfig getInstance(Document doc) {
		if (mailConfig == null) {
			mailConfig = new RerunMailConfig(doc);
		}

		return mailConfig;
	}

	private boolean generateEnable() {
		String result = getNodeString("/rerunConfig/mail/Enable");
		if (isNullOrEmpty(result)) {
			return false;
		}
		return Boolean.parseBoolean(result.trim());
	}

	private String generateSTMPHost() {
		if (enable) {
			return getNodeString("/rerunConfig/mail/SMTPHost");
		}
		return null;
	}

	private String generateSubject() {
		if (enable) {
			return getNodeString("/rerunConfig/mail/Subject");
		}
		return null;
	}

	private String generateFrom() {
		if (enable) {
			return getNodeString("/rerunConfig/mail/From");
		}
		return null;
	}

	private List<String> generateToList() {
		if (enable) {
			return getNodeStringList("/rerunConfig/mail/ToList/String");
		}
		return null;
	}

	private List<String> generateCCList() {
		if (enable) {
			return getNodeStringList("/rerunConfig/mail/CCList/String");
		}
		return null;
	}

	public boolean isEnable() {
		return enable;
	}

	public String getsTMPHost() {
		return sTMPHost;
	}

	public String getSubject() {
		return subject;
	}

	public String getFrom() {
		return from;
	}

	public List<String> getToList() {
		return toList;
	}

	public List<String> getCcList() {
		return ccList;
	}

}
