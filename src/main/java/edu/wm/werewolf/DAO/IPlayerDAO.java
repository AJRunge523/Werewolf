package edu.wm.werewolf.DAO;

import java.util.ArrayList;
import java.util.List;

import edu.wm.werewolf.domain.GPSLocation;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.domain.SimplePlayer;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;

public interface IPlayerDAO {

	public void insertPlayer(Player player);
	
	public List<SimplePlayer> getAll();
	
	public List<Player> getAllFull();
	
	public Player getPlayerByName(String id);

	public void setPlayerLocation(String id, GPSLocation location);

	public void dropAll();

	public List<Player> getNearbyPlayers(String id);

	public boolean inRange(String killerID, String victimID);

	public void generateKill(Player killer, Player victim);

	public int checkGameState();

	public void vote(Player voter, Player voted);

}
