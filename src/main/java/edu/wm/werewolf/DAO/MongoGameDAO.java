package edu.wm.werewolf.DAO;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import edu.wm.werewolf.domain.Game;
import edu.wm.werewolf.domain.Player;

public class MongoGameDAO implements IGameDAO {

@Autowired private MongoTemplate mongoTemplate;
	@Override
	public boolean isNight() {

			Game game = mongoTemplate.findOne(query(where("_class").is("edu.wm.werewolf.domain.Game")), Game.class);
			if(game==null)
				return false;
			System.out.println(game.isNight());
			return game.isNight();
	}

	@Override
	public void dayNightSwitch() {
		Game game = mongoTemplate.findOne(query(where("_class").is("edu.wm.werewolf.domain.Game")), Game.class);
		if(game==null)
			return;
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
		if(selected!="")
		{
			System.out.println("Someone was killed: " + selected);
			mongoTemplate.updateFirst(query(where("userID").is(selected)), new Update().set("isDead", true), Player.class);
		}
		
		//****Score stuff will go here ********
	}

}
