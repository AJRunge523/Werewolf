package edu.wm.werewolf.exceptions;

public class DuplicateUserException extends Exception {

	private String userID;
	
	private static final long serialVersionUID = 2L;
	
	public DuplicateUserException(String userId)
	{
		super();
		this.userID = userId;
	}
	
	public String getUserID()
	{
		return userID;
	}
	@Override
	public String getMessage()
	{
		return "The username: " + userID + " is already in use. Please select a different username";
	}
}
