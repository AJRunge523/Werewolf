package edu.wm.werewolf.domain;

public class PlayerTarget {
	
	private String userID;
	private boolean isDead;
	private int distance;
	
	public PlayerTarget(Player p, int distance)
	{
		this.userID = p.getUserID();
		this.isDead = p.isDead();
		this.distance = distance;
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

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
}
