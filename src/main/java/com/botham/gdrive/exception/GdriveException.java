package com.botham.gdrive.exception;

public class GdriveException extends Exception {

	private static final long serialVersionUID = 4042173965107639841L;
	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public GdriveException(String message) {
		
		this.message=message;
		
	}
}
