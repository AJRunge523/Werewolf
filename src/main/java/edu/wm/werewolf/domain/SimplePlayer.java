package edu.wm.werewolf.domain;

public class SimplePlayer {

	private String userID;
	private boolean isDead;
	
	public SimplePlayer(Player p)
	{
		this.userID = p.getUserID();
		this.isDead = p.isDead();
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
}
