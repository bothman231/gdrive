package com.botham.gdrive;

public class GdriveResult {

	public GdriveResult() {

	}
	
	
	
	
	public GdriveResult(String resultMessage) {
       this.resultMessage=resultMessage;
	}

	private String resultMessage = "";

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

}
