package com.botham.gdrive;

import java.io.Serializable;
import java.util.List;

import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.File;

public class GdriveResult implements Serializable {

	private static final long serialVersionUID = -4692893037589984302L;

	public GdriveResult() {

	}
	
	private String resultMessage = "";
	private About about;
	private List<File> files;
	
	
	public List<File> getFiles() {
		return files;
	}



	public void setFiles(List<File> files) {
		this.files = files;
	}



	public About getAbout() {
		return about;
	}



	public void setAbout(About about) {
		this.about = about;
	}

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
