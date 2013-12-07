package edu.wm.werewolf.DAO;

import java.util.List;

import edu.wm.werewolf.domain.MyUser;
import edu.wm.werewolf.exceptions.DuplicateUserException;

public interface IUserDAO {

	public void createUser(MyUser user) throws DuplicateUserException;

	public MyUser getUserByName(String userName);
	
	public List<MyUser> getAllUsers();

	public void createNewGame(int cycleTime);

	public boolean removeUserById(String username);
}
