package edu.wm.werewolf.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import edu.wm.werewolf.HomeController;
import edu.wm.werewolf.DAO.IGameDAO;
import edu.wm.werewolf.DAO.IPlayerDAO;
import edu.wm.werewolf.DAO.IUserDAO;
import edu.wm.werewolf.domain.GPSLocation;
import edu.wm.werewolf.domain.JsonResponse;
import edu.wm.werewolf.domain.MyUser;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.domain.PlayerTarget;
import edu.wm.werewolf.domain.SimplePlayer;

public class GameService {

	@Autowired private IPlayerDAO playerDao;
	@Autowired private IUserDAO userDao;
	@Autowired private IGameDAO gameDao;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	public List<SimplePlayer> getAll()
	{
		return playerDao.getAll();
	}
	
	public List<Player> getAllFull()
	{
		return playerDao.getAllFull();
	}
	
	public Player getPlayerByName(String name)
	{
		return playerDao.getPlayerByName(name);
	}
	
	public boolean canKill(Player killer, Player victim)
	{
		return true;
	}
	
	public List<MyUser> getAllUsers()
	{
		return userDao.getAllUsers();
	}
	
	public MyUser getUserByName(String name)
	{
		return userDao.getUserByName(name);
	}
	
	public JsonResponse killPlayerByName(String killerID, String victimID)
	{
		Player killer, victim;
		if((killer=getPlayerByName(killerID))!=null && (victim=getPlayerByName(victimID))!=null)
		{
			if(!killer.isWerewolf())
			{
				return new JsonResponse("failure", "1 Illegal operation: kill");
			}
			if(!playerDao.inRange(killerID, victimID))
			{
				return new JsonResponse("failure", "2 Player not in range");
			}
			else if(victim.isDead())
			{
				return new JsonResponse("failure", "3 player is already dead");
			}
			else if(victim.isWerewolf())
			{
				return new JsonResponse("failure", "4 Cannot kill another werewolf");
			}
			else if(!gameDao.isNight())
			{
				return new JsonResponse("failure", "5 Cannot kill during the day");
			}
			else if(!killer.getVoteID().equals(""))
			{
				return new JsonResponse("failure", "6 Already killed one person today");
			}
			else
			{
				playerDao.generateKill(killer, victim);
				int state = playerDao.checkGameState();
				if(state==0 || state==1)
				{
					endGame(state);
				}
				return new JsonResponse("success", victim.getUserID());
			}
		}
		else
			return new JsonResponse("failure", "7 Invalid Target or Killer");

		
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
		gameDao.reloadGame();
	}
	
	public JsonResponse votePlayerByName(String voterID, String votedID){
		Player voter, voted;
		if((voter=getPlayerByName(voterID))!=null && (voted=getPlayerByName(votedID))!=null)
		{
			if(!voter.isDead() && !voted.isDead())
			{
				if(!gameDao.isNight())
				{
					playerDao.vote(voter, voted);
					return new JsonResponse("success", voted);
				}
				else
					return new JsonResponse("failure", "1 Cannot vote at night");
			}
			else
				return new JsonResponse("failure", "2 invalid vote");
		}
		else
			return new JsonResponse("failure", "3 Invalid player ID");
		
	}

	public void updatePosition(String userName, GPSLocation location) {
		MyUser u = userDao.getUserByName(userName);
		playerDao.setPlayerLocation(u.getId(), location);
	}
	
	public List<String> getGameState()
	{
		List<String> gameState = gameDao.getGameState();
		return gameState;
	}
	
	public boolean isGameRunning()
	{
		return gameDao.isRunning();
	}
	
	@Scheduled(fixedDelay=300000)
	private void checkLocationUpdates()
	{
		//logger.info("beep");
	}

	public List<PlayerTarget> getNearbyPlayers(String name){
		MyUser u = userDao.getUserByName(name);
		return playerDao.getNearbyPlayers(u.getId());
	}
	
	@Scheduled(fixedDelay=5000)
	public void dayNightSwitch()
	{
		String s = gameDao.dayNightSwitch();
		int x = playerDao.checkGameState();
		if(x != 2)
			endGame(x);
	}
	

}
