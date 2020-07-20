package com.ananotherrpg.level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ananotherrpg.entity.Player;
import com.ananotherrpg.entity.QuestMananger;
import com.ananotherrpg.entity.dialogue.DialogueManager;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.util.Graph;

public class Campaign {

	private int campaignId;
	private String introduction;
	
	private Player player;

	public boolean isComplete;

	public Campaign(int campaignId, String introduction, List<Quest> campaignQuests, List<Quest> activeQuests, List<Quest> completedQuests, Graph<Location> locations, Location currentLocation,
			Player player, boolean isComplete) {
		super();

		this.player = player;
		player.attachManagers(new LocationManager(locations, currentLocation), new DialogueManager(), new QuestMananger(campaignQuests, activeQuests, completedQuests));

		this.campaignId = campaignId;
		this.introduction = introduction;

	}

	public Campaign(int campaignId, String introduction, List<Quest> campaignQuests, Graph<Location> locations, Location currentLocation,
			Player player, boolean isComplete) {
		super();

		this.player = player;
		player.attachManagers(new LocationManager(locations, currentLocation), new DialogueManager(), new QuestMananger(campaignQuests, new ArrayList<Quest>(), new ArrayList<Quest>()));

		this.campaignId = campaignId;
		this.introduction = introduction;

	}
	
	public void loop() {
		player.loop();

	}

	public void start() {
		IOManager.println(introduction);
	}

	
	public boolean shouldExitCampaign() {
		return player.shouldExitCampaign();
	}
	
	public static Campaign loadCampaignFromXML(File file) {
		return null;
	}
}
