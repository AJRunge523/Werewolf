package edu.wm.werewolf.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "kanjiLists")
public class KanjiList {
	
	private String listName;
	private int count;
	private String cardString;
	public KanjiList(String listName, int count, String cardString) {
		super();
		this.listName = listName;
		this.count = count;
		this.cardString = cardString;
	}
	public KanjiList() {
		super();
		this.listName = "";
		this.count = 0;
		this.cardString = "";
	}
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCardString() {
		return cardString;
	}
	public void setCardString(String cardString) {
		this.cardString = cardString;
	}
	
}
