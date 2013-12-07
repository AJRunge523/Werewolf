package edu.wm.werewolf.DAO;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

import edu.wm.werewolf.config.SpringMongoConfig;
import edu.wm.werewolf.domain.Game;
import edu.wm.werewolf.domain.Kill;
import edu.wm.werewolf.domain.Player;

public class MongoGameDAO implements IGameDAO {

	ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
	MongoTemplate mongoTemplate = ctx.getBean(MongoTemplate.class);
	private Game game;
	@Override
	public boolean isNight() {
		if(game==null)	
			game = mongoTemplate.findOne(query(where("_class").is("edu.wm.werewolf.domain.Game")), Game.class);
		if(game==null)
			return false;
		System.out.println(game.isNight());
		return game.isNight();
	}
	
	public void reloadGame() {
		game = mongoTemplate.findOne(query(where("_class").is("edu.wm.werewolf.domain.Game")), Game.class);
	}
	
	public boolean isRunning() {
		if(game==null)	
			game = mongoTemplate.findOne(query(where("_class").is("edu.wm.werewolf.domain.Game")), Game.class);
		if(game==null)
			return false;
		System.out.println(game.isRunning());
		return game.isRunning();
	}

	@Override
	public String dayNightSwitch() {
		if(game==null)
			game = mongoTemplate.findOne(query(where("_class").is("edu.wm.werewolf.domain.Game")), Game.class);
		if(game==null)
			return "Failed to connect to database";
		boolean isNight = isNight();
		game.incrementNight();
		System.out.println(isNight +", Actuallity: " + game.isNight());
		//Need to clear the killed targets from the werewolves.
		if(isNight && !game.isNight()) //Switch from night to day
		{
			mongoTemplate.updateFirst(query(where("_class").is("edu.wm.werewolf.domain.Game")),
					new Update().set("isNight", false),Game.class);
			System.out.println("Switching from night to day!!!!");
			mongoTemplate.updateMulti(query(where("isWerewolf").is(true)), new Update().set("voteID", ""), Player.class);
			//mongoTemplate.getDb().getMongo().close();
		}
		//Need to clear the voted targets, kill the 
		else if(!isNight && game.isNight()) //Switch from day to night
		{
			mongoTemplate.updateFirst(query(where("_class").is("edu.wm.werewolf.domain.Game")),
					new Update().set("isNight", true),Game.class);
			System.out.println("switching from day to night!!!!");
			hangPlayer();
			mongoTemplate.updateMulti(query(where("_class").is("edu.wm.werewolf.domain.Player")), new Update().set("voteID", ""), Player.class);
			//mongoTemplate.getDb().getMongo().close();
		}
		return "success";
	}

	private void hangPlayer() {
		List<Player> players = mongoTemplate.find(query(where("isDead").is(false)), Player.class);
		System.out.println(players.size());
		HashMap<String, Integer> votes = new HashMap<String, Integer>();
		for(Player p : players)
		{
			if(!(p.getVoteID().equals("")))
			{
				if(!votes.containsKey(p.getVoteID()))
				{
				votes.put(p.getVoteID(), 1);
				}
				else
					votes.put(p.getVoteID(), votes.get(p.getVoteID())+1);
			}
		}
		String selected = "";
		int voteMax = 0;
		for(String s: votes.keySet())
		{
			if(votes.get(s)>voteMax)
			{
				selected = s;
				voteMax = votes.get(s);
			}
		}
		if(!selected.equals(""))
		{
			Player p = mongoTemplate.findOne(query(where("userID").is(selected)), Player.class);
			System.out.println("Someone was killed: " + selected);
			mongoTemplate.updateFirst(query(where("userID").is(selected)), new Update().set("isDead", true), Player.class);
			Kill k = new Kill("Mob", selected, new Date(), 0, 0, 1, p.isWerewolf());
			mongoTemplate.insert(k);
		}

		
		//****Score stuff will go here ********
	}

	public boolean smitePlayer(String id) {
		System.out.println("The id is: " + id +"---");
		Player p = mongoTemplate.findOne(query(where("userID").is(id)), Player.class);
		if(p!=null)
		{
			System.out.println("THE USER EXISTS WOOP WOOP");
			if(!p.isDead())
			{
				WriteResult wr = mongoTemplate.updateFirst(query(where("userID").is(id)),  new Update().set("isDead", true), Player.class);
				Kill k = new Kill("God", id, new Date(), 
					0, 0, 2, p.isWerewolf());
				mongoTemplate.insert(k);
			}
		}
		return false;
	}
	
	public List<Kill> getKills()
	{
		return mongoTemplate.findAll(Kill.class);
	}
	
	@Override
	public List<String> getGameState() {
		List<String> stats = new ArrayList<String>();
		if(game==null)
			game = mongoTemplate.findOne(query(where("_class").is("edu.wm.werewolf.domain.Game")), Game.class);
		if(game==null)
			return null;
		if(!game.isRunning())
		{
			stats.add("no");
		}
		if(game.isNight())
			stats.add("night");
		else
			stats.add("day");
		stats.add(String.valueOf(game.timeToNextNight()));
		stats.add(String.valueOf(game.getCreatedDate()));
		return stats;
	}

}
