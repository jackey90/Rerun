package com.ea.rerun.feedback.model;

public class ReportCell {
	private String tdAttrs;
	private String cellAAttrs;
	private String cellValue;

	public ReportCell(String tdAttrs, String cellAAttrs, String cellValue) {
		if (tdAttrs == null) {
			tdAttrs = "";
		}
		if (cellAAttrs == null) {
			cellAAttrs = "";
		}
		if (cellValue == null) {
			cellValue = "";
		}
		this.tdAttrs = tdAttrs;
		this.cellAAttrs = cellAAttrs;
		this.cellValue = cellValue;
	}

	public String getTdAttrs() {
		return tdAttrs;
	}

	public void setTdAttrs(String tdAttrs) {
		this.tdAttrs = tdAttrs;
	}

	public String getCellAAttrs() {
		return cellAAttrs;
	}

	public void setCellAAttrs(String cellAAttrs) {
		this.cellAAttrs = cellAAttrs;
	}

	public String getCellValue() {
		return cellValue;
	}

	public void setCellValue(String cellValue) {
		this.cellValue = cellValue;
	}

}
