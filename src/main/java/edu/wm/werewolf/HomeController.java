package edu.wm.werewolf;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.wm.werewolf.domain.JsonResponse;
import edu.wm.werewolf.domain.MyUser;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.exceptions.DuplicateUserException;
import edu.wm.werewolf.service.GameService;
import edu.wm.werewolf.service.IUserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	//This line automatically wires an instance of an GameService,
	//which was bound to a singleton instance of the class, GameService, in the root-context.xml file.
	@Autowired private GameService gameService;
	@Autowired private IUserService userService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		return "home";
	}
	@RequestMapping(value = "/newAccount", method = RequestMethod.POST)
	public @ResponseBody JsonResponse takeActionOnPlayer(@RequestBody Map<String, String> body) 
	{
		try {
		userService.createUser(body.get("firstName"), body.get("lastName"), 
				body.get("userName"), body.get("password"), body.get("role"));
		}
		catch(DuplicateUserException e)
		{
			return new JsonResponse("failure", e.getMessage());
		}
		return new JsonResponse("success", null);
	}
	@RequestMapping(value = "/auth/restart", method = RequestMethod.POST)
	public @ResponseBody JsonResponse restartGame(@RequestBody Map<String, Integer> cycle)
	{
		logger.info("asdjiojtoewjrsdlfkjldjsf");
		
		gameService.restartGame(cycle.get("time"));
		return new JsonResponse("success", null);
	}
	@RequestMapping(value = "/switch", method = RequestMethod.GET)
	public @ResponseBody JsonResponse switchTimes()
	{
		logger.info("attempting to switch from night to day");
		gameService.dayNightSwitch();
		return new JsonResponse("success", null);
	}
	
	@RequestMapping(value = "/auth/verify", method = RequestMethod.GET)
	public @ResponseBody JsonResponse verifyUser(Principal principal)
	{
		HashMap<String, String> response = new HashMap<String, String>();
		MyUser user = gameService.getUserByName(principal.getName());
		if(user==null)
			return new JsonResponse("failure", "User not found");
		else
		{
			Player player = gameService.getPlayerByName(principal.getName());
			String s = user.getAuthorities().toString();
			s = s.substring(1, s.length()-1);
			response.put("ROLE", s);
			if(player==null)
				response.put("TYPE", "null");
			else
			{
				response.put("TYPE", player.isWerewolf() ? "WOLF" : "TOWN");
				response.put("STATUS", player.isDead() ? "DEAD" : "ALIVE");
			}
		}
		return new JsonResponse("success", response);	
	}
	@RequestMapping(value = "/auth/game", method = RequestMethod.GET)
	public @ResponseBody JsonResponse getGameStats(Principal principal)
	{
		HashMap<String, String> response = new HashMap<String, String>();
		List<Player> players = gameService.getAllFull();
		if(players == null)
			return new JsonResponse("failure", "Database Error");
		int aliveCount = 0, ww = 0;
		for(Player x: players)
		{
			if(!x.isDead())
			{
				aliveCount++;
				System.out.println(x.getUserID() + ", " + x.isDead());
			}
			if(x.isWerewolf() && !x.isDead())
				ww++;
		}
		List<String> gameState = gameService.getGameState();
		if(gameState == null)
			return new JsonResponse("failure", "Database Error");
		else
		{
			response.put("alive", String.valueOf(aliveCount));
			response.put("players", String.valueOf(players.size()));
			response.put("ww", String.valueOf(ww));
			response.put("time", gameState.get(0));
			response.put("left", gameState.get(1));
			return new JsonResponse("success", response);
		}
		
	}
	
}
