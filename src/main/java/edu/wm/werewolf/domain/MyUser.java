package edu.wm.werewolf.domain;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Document(collection = "users")
public class MyUser extends User{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7074331855644684967L;
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String username;
	private String imageURL;
	private String hashedPassword;
	private int score;
	public MyUser(String firstName, String lastName, String username,
			String hashedPassword, String imageURL, Collection<GrantedAuthority> authorities, int score) {
		super(username, BCrypt.hashpw(hashedPassword, BCrypt.gensalt()), true, true, true, true, authorities);
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.imageURL = imageURL;
		this.hashedPassword = this.getPassword();
		this.score=score;
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getHashedPassword() {
		return hashedPassword;
	}
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

	
}
