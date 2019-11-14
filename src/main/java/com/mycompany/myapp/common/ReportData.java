package com.mycompany.myapp.common;


import java.io.Serializable;
import java.util.List;

public class ReportData implements Serializable {

	private static final long serialVersionUID = 2841927473422237002L;

	/**
	 * This field is used to print signers name below the signature in PDF
	 * report that provides feature of auto-sign
	 **/
	private String signerName;

	/**
	 * This field is used to show title of Report
	 **/
	private String reportTitle;

	/**
	 * This field is used to show the FHLBNY logo on PDF report
	 **/
	private String logo;

	/**
	 * This field is used to exclude any column from PDF report
	 **/
	private List<String> excludeFields;

	/**
	 * This field contains the list of headers of PDF headers
	 **/
	private List<String> headers;

	/**
	 * This field contains list of Records of any type of class or datatype to
	 * show data on PDF report
	 **/
	private List<?> data;

	/**
	 * This field is used to show username on pdf report
	 **/
	private String userName;

	/**
	 * This field is used to add signature in PDF report that provides feature
	 * of auto-sign. This is Base64String of user signature.
	 **/
	private String userSignature;

	public String getSignerName() {
		return signerName;
	}

	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public List<String> getExcludeFields() {
		return excludeFields;
	}

	public void setExcludeFields(List<String> excludeFields) {
		this.excludeFields = excludeFields;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSignature() {
		return userSignature;
	}

	public void setUserSignature(String userSignature) {
		this.userSignature = userSignature;
	}
}
