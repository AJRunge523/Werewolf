package edu.wm.werewolf.exceptions;

public class NoPlayerFoundException extends Exception {

	private String userID;
	
	private static final long serialVersionUID = 2L;
	
	public NoPlayerFoundException(String userId)
	{
		super();
		this.userID = userId;
	}
	
	public String getUserID()
	{
		return userID;
	}
}
