package edu.wm.werewolf.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "games")
public class Game {
	
	@Id
	private int id;
	private Date createdDate;
	private int dayNightFreq;
	private boolean isRunning;
	private boolean isNight; 
	
	public Game(Date createdDate, int dayNightFreq) {
		super();
		this.createdDate = createdDate;
		this.dayNightFreq = dayNightFreq;
		this.isRunning=true;
		id=1;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getDayNightFreq() {
		return dayNightFreq;
	}
	public void setDayNightFreq(int dayNightFreq) {
		this.dayNightFreq = dayNightFreq;
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	public boolean incrementNight()
	{
		Date d = new Date();
		long dt = d.getTime() - this.createdDate.getTime();
		System.out.println(d.getTime() + ", " + this.createdDate.getTime() + ", " + dt/dayNightFreq);
		if(((int)dt/dayNightFreq) % 2 == 0)
		{
			setNight(true);
			return true;
		}
		setNight(false);
		return false;
	}
	public void setNight(boolean isNight) {
		this.isNight = isNight;
	}
	public boolean isNight() {
		return this.isNight;
	}
}
