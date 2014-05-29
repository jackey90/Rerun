package com.ea.rerun.feedback.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportModel {
	private Map<String, String> fields;
	private List<String> headers;
	private List<List<ReportCell>> bodys;
	
	public ReportModel(){
		fields = new LinkedHashMap<String, String>();
		headers = new ArrayList<String>();
		bodys = new ArrayList<List<ReportCell>>();
	}
	
	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<List<ReportCell>> getBodys() {
		return bodys;
	}

	public void setBodys(List<List<ReportCell>> bodys) {
		this.bodys = bodys;
	}

	@Override
	public String toString() {
		return "ReportModel [fields=" + fields + ", headers=" + headers
				+ ", bodys=" + bodys + "]";
	}

}
