package com.ananotherrpg.level;

import java.io.File;
import java.util.LinkedList;

import com.ananotherrpg.entity.Player;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.util.Graph;

public class Campaign {
	private int campaignId;

	private String introduction;
	
	private Graph<Location> locations;
	private Location currentLocation;
	
	private LinkedList<Quest> questsLog;
	private Quest currentQuest;

	private Player player;
	
	private Boolean isComplete;
	
	public Campaign(int campaignId, String introduction, Graph<Location> locations, Location currentLocation, Quest beginningQuest) {
		this.introduction = introduction;
		this.campaignId = campaignId;
		this.locations = locations;
		this.currentLocation = currentLocation;
		this.currentQuest = beginningQuest;
		this.player = new Player("Mark", 20, 20, new Inventory(), 5, 1);
	}

	public Campaign(int campaignId, String introduction, Graph<Location> locations, Location currentLocation, LinkedList<Quest> quests,
			Quest currentQuest, Player player) {
		super();
		this.introduction = introduction;
		this.campaignId = campaignId;
		this.locations = locations;
		this.currentLocation = currentLocation;
		this.questsLog = quests;
		this.currentQuest = currentQuest;
		this.player = player;
	}
	
	public static Campaign loadCampaignFromXML(File file) {
		return null;
	}
	
	
	public Graph<Location> getLocations() {
		return locations;
	}

	public void setLocations(Graph<Location> locations) {
		this.locations = locations;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public LinkedList<Quest> getQuestsLog() {
		return questsLog;
	}

	public void setQuestsLog(LinkedList<Quest> questsLog) {
		this.questsLog = questsLog;
	}

	public Quest getCurrentQuest() {
		return currentQuest;
	}

	public void setCurrentQuest(Quest currentQuest) {
		this.currentQuest = currentQuest;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setCurrentLocation(Location location) {
		this.currentLocation = location;
		
	}
}
