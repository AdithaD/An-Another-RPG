package com.ananotherrpg.level;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.Identifiable;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.Player;
import com.ananotherrpg.entity.dialogue.DialogueManager;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.util.Graph;

public class Campaign {

	private int campaignId;
	private String introduction;
	
	private Map<Integer, Quest> questLookup;
	
	private Graph<Location> locations;
	private Player player;

	public boolean isComplete;

	public Campaign(int campaignId, String introduction, List<Quest> campaignQuests, Graph<Location> locations,
			Location currentLocation, List<Quest> activeQuests, Player player, boolean isComplete) {
		super();
		this.introduction = introduction;
		this.campaignId = campaignId;
		this.questLookup = campaignQuests.stream().collect(Collectors.toMap(Quest::getId, Function.identity()));

		this.locations = locations;
		this.player = player;
		this.isComplete = isComplete;

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
