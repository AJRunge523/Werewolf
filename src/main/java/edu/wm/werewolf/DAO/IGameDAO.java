package edu.wm.werewolf.DAO;

import java.util.List;

public interface IGameDAO {

	public boolean isNight();
	
	public String dayNightSwitch();

	public List<String> getGameState();
	
	public boolean isRunning();
	
	public void reloadGame();
}
