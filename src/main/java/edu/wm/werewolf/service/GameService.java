package edu.wm.werewolf.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wm.werewolf.HomeController;
import edu.wm.werewolf.DAO.IGameDAO;
import edu.wm.werewolf.DAO.IPlayerDAO;
import edu.wm.werewolf.DAO.IUserDAO;
import edu.wm.werewolf.domain.GPSLocation;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.domain.MyUser;
import edu.wm.werewolf.domain.SimplePlayer;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;

public class GameService {

	@Autowired private IPlayerDAO playerDao;
	@Autowired private IUserDAO userDao;
	@Autowired private IGameDAO gameDao;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	public List<SimplePlayer> getAllAlive()
	{
		return playerDao.getAllAlive();
	}
	
	public Player getPlayerByName(String name) throws NoPlayerFoundException
	{
		return playerDao.getPlayerByName(name);
	}
	
	public boolean canKill(Player killer, Player victim)
	{
		return true;
	}
	
	public String killPlayerByName(String killerID, String victimID) throws NoPlayerFoundException
	{
		Player killer, victim;
		if((killer=getPlayerByName(killerID))!=null && (victim=getPlayerByName(victimID))!=null)
		{
			if(!killer.isWerewolf())
			{
				return "Failure: Illegal operation: kill";
			}
			if(!playerDao.inRange(killerID, victimID))
			{
				return "Failure: Player not in range";
			}
			else if(victim.isDead())
			{
				return "Failure: player is already dead";
			}
			else if(victim.isWerewolf())
			{
				return "Failure: Cannot kill another werewolf";
			}
			else if(!gameDao.isNight())
			{
				return "Failure: Cannot kill during the day";
			}
			else if(!killer.getVoteID().equals(""))
			{
				return "Failure: Already killed one person today";
			}
			else
			{
				playerDao.generateKill(killer, victim);
			}
		}
		int state = playerDao.checkGameState();
		if(state==0 || state==1)
		{
			endGame(state);
		}
		return "success";
		
	}

	/**
	 * Do things such as drop the player and kill tables, drop the game table
	 * Update user's total scores based on the kill tables/player tables.
	 * @param state
	 */
	private void endGame(int state) {
		//More to come here...
		System.out.println("Game over!");
	}

	public void restartGame(int cycleTime)
	{
		playerDao.dropAll();
		userDao.createNewGame(cycleTime);
	}
	
	public String votePlayerByName(String voterID, String votedID) throws NoPlayerFoundException {
		Player voter, voted;
		if((voter=getPlayerByName(voterID))!=null && (voted=getPlayerByName(votedID))!=null)
		{
			if(!voter.isDead() && !voted.isDead())
			{
				if(!gameDao.isNight())
				{
					playerDao.vote(voter, voted);
					return "success";
				}
				else
					return "Failure: Cannot vote at night";
			}
			else
				return "Failure, invalid vote";
		}
		else
			return "Invalid player ID";
		
	}

	public void updatePosition(String userName, GPSLocation location) {
		MyUser u = userDao.getUserByName(userName);
		playerDao.setPlayerLocation(u.getId(), location);
	}
	
	@Scheduled(fixedDelay=300000)
	private void checkLocationUpdates()
	{
		//logger.info("beep");
	}

	public List<Player> getNearbyPlayers(String name) throws NoPlayerFoundException {
		MyUser u = userDao.getUserByName(name);
		return playerDao.getNearbyPlayers(u.getId());
	}
	
	@Scheduled(fixedDelay=5000)
	public void dayNightSwitch()
	{
		gameDao.dayNightSwitch();
		int x = playerDao.checkGameState();
		if(x != 2)
			endGame(x);
	}
	

}
