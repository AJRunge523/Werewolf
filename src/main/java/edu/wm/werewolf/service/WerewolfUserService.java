package edu.wm.werewolf.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import edu.wm.werewolf.DAO.IUserDAO;
import edu.wm.werewolf.domain.MyUser;
import edu.wm.werewolf.exceptions.DuplicateUserException;

public class WerewolfUserService implements UserDetailsService, IUserService {

	@Autowired private IUserDAO userDao;
	
	@Override
	public void createUser(String firstname, String lastname, String username,
			String password, String role) throws DuplicateUserException{
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role));
		MyUser u = new MyUser(firstname, lastname, username, password, "user.png", authorities, 0);
		userDao.createUser(u);

	}

	@Override
	public void removeUser(String username) {
		userDao.removeUserById(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username)
	{
		MyUser u = userDao.getUserByName(username);
		System.out.println(u.getFirstName());
		if(u==null)
			throw new UsernameNotFoundException(username);
		System.out.println("hi");
		return u;
	}

}
