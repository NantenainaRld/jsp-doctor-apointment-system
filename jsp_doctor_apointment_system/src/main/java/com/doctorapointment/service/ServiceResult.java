package com.doctorapointment.service;

public class ServiceResult {
	public boolean success;
	public String errorMessage = "";

	public ServiceResult(boolean success, String errorMessage) {
		this.success = success;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return this.success;
	}
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
