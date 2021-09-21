package com.otsi.retail.taxMaster.errors;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

	private Integer errorCode;
	private String errorDesc;
	private Date date;
	private HttpStatus httpStatus;

	public ErrorResponse() {
		super();
	}

	public ErrorResponse(Integer errorCode, String errorDesc, Date date) {
		super();
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
		this.date = date;
	}

	public ErrorResponse(Integer errorCode, String errorDesc, Date date, HttpStatus httpStatus) {
		super();
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
		this.date = date;
		this.httpStatus = httpStatus;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}

