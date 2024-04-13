package com.southern.cmps.response;

import lombok.Data;

@Data
public class CmpsResponse {
	
	public enum Status {
		SUCCESS, WARNING, FAILURE, ERROR
	}
	
	private Status statusCode;
	private String message;
	private Object response;
	
	public CmpsResponse(Status statusCode, String message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}

	public CmpsResponse(Status statusCode, String message, Object response) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.response = response;
	}
	
	public CmpsResponse() {
		super();
	}

}
