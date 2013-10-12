package edu.wm.werewolf.domain;

public class JsonResponse {

	private final String success = "SUCCESS";
	private final String failure = "FAILURE";
	private String status;
	
	public JsonResponse(){
		super();
		this.status=success;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
