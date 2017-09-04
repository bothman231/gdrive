package com.botham.gdrive;

public class GdriveResult {

	public GdriveResult() {

	}
	
	private String resultMessage = "";
	private String output = "";
	
	public String getOutput() {
		return output;
	}



	public void setOutput(String output) {
		this.output = output;
	}



	public GdriveResult(String resultMessage) {
       this.resultMessage=resultMessage;
	}



	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

}
