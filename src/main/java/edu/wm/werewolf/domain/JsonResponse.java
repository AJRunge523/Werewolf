package edu.wm.werewolf.domain;

public class JsonResponse {

	private String status;
	private Object data;
	
	public JsonResponse(String status, Object data){
		super();
		this.status=status;
		this.setData(data);
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
