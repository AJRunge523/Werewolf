package edu.wm.werewolf.DAO;

import java.util.List;

import edu.wm.werewolf.domain.KanjiList;
import edu.wm.werewolf.domain.Kill;

public interface IGameDAO {

	public boolean isNight();
	
	public String dayNightSwitch();

	public List<String> getGameState();
	
	public boolean isRunning();
	
	public void reloadGame();
	
	public boolean smitePlayer(String id);
	
	public List<Kill> getKills();
	
	public void addKanjiList(KanjiList list);
	
	public List<KanjiList> getAllLists();
}
