package edu.wm.werewolf.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "players")
public class Player {
	@Id
	private String id;
	private boolean isDead;
	private String userID;
	private boolean isWerewolf;
	private String voteID;
	private double[] location;
	private int score;
	private Date lastUpdate;
	public Player(String id, boolean isDead, double lat, 
			double lon, String userID, boolean isWerewolf, int score) {
		super();
		this.id = id;
		this.isDead = isDead;
		setLocation(new double[] {lat, lon});
		this.userID = userID;
		this.isWerewolf = isWerewolf;
		this.setVoteID("");
		this.setScore(score);
	
	}
	@PersistenceConstructor
	public Player(String id, boolean isDead, double[] location, String userID, boolean isWerewolf)
	{
		super();
		this.id = id;
		this.isDead = isDead;
		this.setLocation(location);
		this.userID = userID;
		this.isWerewolf = isWerewolf;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isDead() {
		return isDead;
	}
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public boolean isWerewolf() {
		return isWerewolf;
	}
	public void setWerewolf(boolean isWerewolf) {
		this.isWerewolf = isWerewolf;
	}
	public String getVoteID() {
		return voteID;
	}
	public void setVoteID(String voteID) {
		this.voteID = voteID;
	}
	public double[] getLocation() {
		return location;
	}
	public void setLocation(double[] location) {
		this.location = location;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
