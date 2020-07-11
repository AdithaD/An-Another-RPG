package com.ananotherrpg.level;

import java.util.List;

import com.ananotherrpg.entity.Entity;

public class Campaign {
	private int campaignId;
	
	private List<Location> locations;
	private Location currentLocation;
	
	private List<Quest> quests;
	private Quest currentQuest;

	private List<Entity> entities;
	
	private Boolean isComplete;
}
