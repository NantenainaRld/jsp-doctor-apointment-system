package com.doctorapointment.service;

public class ServiceResult {
	public boolean success;
	public String errorMessage = "";
	public Object data;

	public ServiceResult(boolean success, String errorMessage, Object data) {
		this.success = success;
		this.errorMessage = errorMessage;
		this.data = data;
	}
	
	public ServiceResult(boolean success, String errorMessage) {
		this(success, errorMessage, null);
	}

	public boolean isSuccess() {
		return this.success;
	}
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
