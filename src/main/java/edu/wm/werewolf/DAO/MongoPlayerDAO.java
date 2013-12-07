package edu.wm.werewolf.DAO;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;

import edu.wm.werewolf.config.SpringMongoConfig;
import edu.wm.werewolf.domain.GPSLocation;
import edu.wm.werewolf.domain.Kill;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.domain.PlayerTarget;
import edu.wm.werewolf.domain.SimplePlayer;

public class MongoPlayerDAO implements IPlayerDAO {

	//@Autowired private MongoTemplate mongoTemplate;
	ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
	MongoTemplate mongoTemplate = ctx.getBean(MongoTemplate.class);
	@Override
	/**
	 * Add player into database
	 */
	public void insertPlayer(Player player) {
		mongoTemplate.insert(player);
	}

	@Override
	/**
	 * Retrieving from database
	 */
	public List<SimplePlayer> getAll() {
		List<Player> p = new ArrayList<Player>();
		p = mongoTemplate.findAll(Player.class);
		List<SimplePlayer> players = new ArrayList<SimplePlayer>();
		for(Player x: p)
			players.add(new SimplePlayer(x));
		return players;
	}
	
	public List<Player> getAllFull() {
		List<Player> p = new ArrayList<Player>();
		p = mongoTemplate.findAll(Player.class);
		return p;
	}

	@Override
	public Player getPlayerByName(String id){
		return mongoTemplate.findOne(query(where("userID").is(id)), Player.class);
	}
	
	public void setPlayerLocation(String id, GPSLocation loc){
		mongoTemplate.getCollection("players").ensureIndex(new BasicDBObject("location", "2d"));
		mongoTemplate.updateFirst(new Query(where("_id").is(id)), 
				new Update().set("location", new double[] {loc.getLat(), loc.getLon()}), Player.class);
		Date d = new Date();
		mongoTemplate.updateFirst(new Query(where("_id").is(id)), 
				new Update().set("lastUpdate", d), Player.class);
	}

	@Override
	public void dropAll() {
		mongoTemplate.dropCollection(Player.class);
	}
	
	@Override
	public List<PlayerTarget> getNearbyPlayers(String id) {
		Player p = mongoTemplate.findOne(query(where("_id").is(id)), Player.class);
		mongoTemplate.getCollection("players").ensureIndex(new BasicDBObject("location", "2d"));
		if(p!=null)
		{
			List<Player> u = mongoTemplate.find(query(where("location").near(new Point(p.getLocation()[0], p.getLocation()[1])).maxDistance(0.001))
				.addCriteria(where("_id").ne(id)), Player.class);
			List<Player> v = mongoTemplate.find(query(where("location").near(new Point(p.getLocation()[0], p.getLocation()[1])).maxDistance(0.003))
					.addCriteria(where("_id").ne(id)), Player.class);
			List<Player> w = mongoTemplate.find(query(where("location").near(new Point(p.getLocation()[0], p.getLocation()[1])).maxDistance(0.003))
					.addCriteria(where("_id").ne(id)), Player.class);
			List<PlayerTarget> pt = new ArrayList<PlayerTarget>();
			for(Player player: w)
				pt.add(new PlayerTarget(player, 2));
			for(PlayerTarget target: pt)
			{
				boolean found = false;
				for(Player player: u)
				{
					if(player.getUserID().equals(target.getUserID()))
					{
						found = true;
						target.setDistance(0);
						break;
					}
				}
				if(found)
					continue;
				for(Player player: v)
				{
					if(player.getUserID().equals(target.getUserID()))
					{
						target.setDistance(1);
						break;
					}
				}
			}
				
			
			return pt;
		}
		else
			return null;
	}

	@Override
	public boolean inRange(String killer, String victim) {
		Player p = mongoTemplate.findOne(query(where("userID").is(killer)), Player.class);
		System.out.println(p);
		Player target = mongoTemplate.findOne(query(where("location").near(new Point(p.getLocation()[0], p.getLocation()[1])).maxDistance(0.001))
				.addCriteria(where("userID").is(victim)), Player.class);
		if(target==null)
			return false;
		System.out.println(target.getUserID());
		return true;
	}

	@Override
	public void generateKill(Player killer, Player victim) {
		WriteResult wr = mongoTemplate.updateFirst(query(where("userID").is(victim.getUserID())),  new Update().set("isDead", true), Player.class);
		System.out.println(wr.getError());
		Kill k = new Kill(killer.getUserID(), victim.getUserID(), new Date(), 
				victim.getLocation()[0], victim.getLocation()[1], 0, false);
		mongoTemplate.insert(k);
		mongoTemplate.updateFirst(query(where("userID").is(killer.getUserID())), 
				new Update().set("voteID", victim.getUserID()), Player.class);
	}

	@Override
	public int checkGameState() {
		if(mongoTemplate.find(query(where("isWerewolf").is(true)).addCriteria(where("isDead").is(false)), Player.class).size()==0)
			return 0;
		else if(mongoTemplate.find(query(where("isWerewolf").is(false)).addCriteria(where("isDead").is(false)), Player.class).size()==0)
			return 1;
		else
			return 2;
	}
	
	@Override
	public void vote(Player voter, Player voted) {
		mongoTemplate.updateFirst(query(where("userID").is(voter.getUserID())), new Update().set("voteID", voted.getUserID()), Player.class);
		
	}
}
