package edu.wm.werewolf.DAO;

import java.util.List;

import edu.wm.werewolf.domain.GPSLocation;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.domain.PlayerTarget;
import edu.wm.werewolf.domain.SimplePlayer;

public interface IPlayerDAO {

	public void insertPlayer(Player player);
	
	public List<SimplePlayer> getAll();
	
	public List<Player> getAllFull();
	
	public Player getPlayerByName(String id);

	public void setPlayerLocation(String id, GPSLocation location);

	public void dropAll();

	public List<PlayerTarget> getNearbyPlayers(String id);

	public boolean inRange(String killerID, String victimID);

	public void generateKill(Player killer, Player victim);

	public int checkGameState();

	public void vote(Player voter, Player voted);

}
