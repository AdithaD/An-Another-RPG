package com.ananotherrpg.level;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.Player;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.util.Graph;

public class Campaign {
	private int campaignId;
	
	private Graph<Location> locations;
	private Location currentLocation;
	
	private LinkedList<Quest> questsLog;
	private Quest currentQuest;

	private Player player;
	
	private Boolean isComplete;
	
	public Campaign(int campaignId, Graph<Location> locations, Location currentLocation, Quest beginningQuest) {
		this.campaignId = campaignId;
		this.locations = locations;
		this.currentLocation = currentLocation;
		this.currentQuest = beginningQuest;
		this.player = new Player("Mark", 20, 20, new Inventory(), 5, 1);
	}

	public Campaign(int campaignId, Graph<Location> locations, Location currentLocation, LinkedList<Quest> quests,
			Quest currentQuest, Player player) {
		super();
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
	
}
