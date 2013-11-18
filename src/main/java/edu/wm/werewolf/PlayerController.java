package edu.wm.werewolf;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.wm.werewolf.domain.GPSLocation;
import edu.wm.werewolf.domain.JsonResponse;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.domain.SimplePlayer;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;
import edu.wm.werewolf.service.GameService;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/auth/players")
public class PlayerController {
	
	private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
	
	//This line automatically wires an instance of an GameService,
	//which was bound to a singleton instance of the class, GameService, in the root-context.xml file.
	@Autowired private GameService gameService;
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody JsonResponse getAll()
	{
		List<SimplePlayer> players = gameService.getAllAlive();
		logger.info("Players: {}", players.toString());
		return new JsonResponse("success", players);
		//return "butts";
	}
	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse getPlayerByID(@PathVariable String name, Principal principal)
	{
		try {
			Player player = gameService.getPlayerByName(name);
			logger.info("ID number received: {}", name);
			return new JsonResponse("success", player);
		} catch (NoPlayerFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Id number received: {}", name);
			return new JsonResponse("failure", e.getMessage());
		}
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse takeActionOnPlayer(@PathVariable String id, @RequestBody Map<String, String> body, Principal principal) {
		
	    logger.info("Body content: " + body.get("type"));
	    if(body.get("type").equals("kill"))
	    {
	    	try{
	    		return new JsonResponse("success", gameService.killPlayerByName(principal.getName(), id));
	    	}
	    	catch(NoPlayerFoundException e) {
	    		logger.info("ID number received: {}", id);
	    		return new JsonResponse("failure", e.getMessage());
	    	}
	    }
	    else if(body.get("type").equals("vote"))
	    {
	    	try{
	    		return new JsonResponse("success", gameService.votePlayerByName(principal.getName(), id));
	    	}
	    	catch(NoPlayerFoundException e) {
	    		logger.info("ID number received: {}", id);
	    		return null;
	    	}
	    }
	    else return new JsonResponse("failure", "invalid action type");
	}
	@RequestMapping(value = "/location", method = RequestMethod.POST)
	public @ResponseBody JsonResponse setLocation(@RequestBody Map<String, Double> body, Principal principal)
	{
		GPSLocation loc = new GPSLocation(body.get("lat"), body.get("lon"));
		logger.info("Setting" + principal.getName() + "'s location to: " + loc);
		gameService.updatePosition(principal.getName(), loc);
		JsonResponse response = new JsonResponse("success", null);
		return response;
	}
	@RequestMapping(value = "/nearbyPlayers", method = RequestMethod.POST)
	public @ResponseBody JsonResponse getNearbyPlayers(@RequestBody Map<String, Double> body, Principal principal)
	{
		List<Player> players;
		JsonResponse response;
		GPSLocation location = new GPSLocation(body.get("lat"), body.get("lon"));
		try {
			if(!gameService.getPlayerByName(principal.getName()).isWerewolf())
			{
				response = new JsonResponse("failure", "Player is not a werewolf");
				return response;
			}
		} catch (NoPlayerFoundException e1) {
			e1.printStackTrace();
			response = new JsonResponse("failure", e1.getMessage());
			return response;
		}
		logger.info("Checking for players near to" + principal.getName() + "s location: " + location);
		gameService.updatePosition(principal.getName(), location);
		try {
			players = gameService.getNearbyPlayers(principal.getName());
		} catch (NoPlayerFoundException e) {
			// TODO Auto-generated catch block
			response = new JsonResponse("failure", e.getMessage());
			e.printStackTrace();
			return response;
		}
		response = new JsonResponse("success", players);
		return response;
	}
}
