package edu.wm.werewolf.service;

import edu.wm.werewolf.exceptions.DuplicateUserException;

public interface IUserService {
	
	public void createUser(String firstname, String lastname, String username, String password, String string) throws DuplicateUserException;
	
	public void removeUser(String username);

}
