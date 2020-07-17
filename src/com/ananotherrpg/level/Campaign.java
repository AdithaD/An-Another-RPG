package com.ananotherrpg.level;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.dialogue.DialogueLine;
import com.ananotherrpg.entity.dialogue.DialogueManager;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.io.InterruptedQueryException;
import com.ananotherrpg.util.Link;

public class Campaign {

	private ArrayList<String> options = new ArrayList<String>(
			Arrays.asList(new String[] { "Look around", "Move to", "Examine", "Talk", "View Quests", "Help" }));

	private int campaignId;
	private String introduction;
	private Map<Integer, Quest> questLookup;

	private CampaignState campaignState;

	private IOManager io;
	private boolean shouldExitCampaign;

	public Campaign(int campaignId, String introduction, List<Quest> campaignQuests, CampaignState campaignState,
			IOManager io) {
		super();
		this.introduction = introduction;
		this.campaignId = campaignId;
		this.questLookup = campaignQuests.stream().collect(Collectors.toMap(Quest::getId, Function.identity()));
		this.campaignState = campaignState;
		this.shouldExitCampaign = false;

		this.io = io;

	}

	private void talk() {
		if (campaignState.currentLocation.getPermanentEntities().isEmpty()) {
			io.println("There is no one to talk to");
		} else {
			try {
				io.println("Who would you like to talk to?");
				Entity target = io.listAndQueryUserInputAgainstIdentifiers(
						campaignState.currentLocation.getPermanentEntities(), ListType.NUMBERED,
						SelectionMethod.NUMBERED);

				initiateDialogue(target);
			} catch (InterruptedQueryException e) {
				io.println("You decide otherwise...");
			}
		}
	}

	private void initiateDialogue(Entity target) {
		try {
			DialogueManager manager = new DialogueManager(target.getDialogueGraph());

			io.println(manager.getDialogue());
			while (manager.hasMoreDialogue()) {
				Map<String, Link<DialogueLine, String>> linkToLinkDataMap = manager.generateLinkToLinkDataMap();
				List<String> optionsText = new ArrayList<String>(linkToLinkDataMap.keySet());

				manager.traverseLink(linkToLinkDataMap.get(io.listAndQueryUserInputAgainstStrings(optionsText,
						ListType.NUMBERED, SelectionMethod.NUMBERED)));
				io.println(manager.getDialogue());
			}

			manager.getNewQuests().forEach(id -> {
				campaignState.activeQuests.add(questLookup.get(id));
			});
		} catch (InterruptedQueryException e) {
			io.println("You decide otherwise...");
		}
	}

	private void moveTo() {
		List<Location> adjacentLocations = campaignState.locations.getAdjacentNodes(campaignState.currentLocation);

		if (adjacentLocations.isEmpty()) {
			io.println("There is no where to go to!");
		} else {
			try {
				io.println("Where would you like to go ?");

				campaignState.setCurrentLocation(io.listAndQueryUserInputAgainstIdentifiers(adjacentLocations,
						ListType.NUMBERED, SelectionMethod.NUMBERED));
				io.println("You move to " + campaignState.currentLocation.getName());

			} catch (InterruptedQueryException e) {
				io.println("You decide otherwise...");
			}
		}

	}

	private void lookAround() {
		io.println(campaignState.currentLocation.getDescription());
		io.println("You do a quick whirl and you see:");

		List<Entity> permanentEntities = campaignState.currentLocation.getPermanentEntities();
		List<ItemStack> itemStacks = campaignState.currentLocation.getItemStacks();
		List<Location> accessibleLocations = campaignState.getAccessibleLocations();

		if (permanentEntities.isEmpty()) {
			io.println("There is noone in this area");
		} else {
			io.listIdentifiers(permanentEntities, ListType.BULLET);
		}

		if (itemStacks.isEmpty()) {
			io.println("There is no items in this area");
		} else {
			io.listIdentifiers(itemStacks, ListType.BULLET);
		}

		if (accessibleLocations.isEmpty()) {
			io.println("I can't see anywhere to go right now");
		} else {
			io.listIdentifiers(accessibleLocations, ListType.BULLET);
		}
	}

	private void viewQuests() {
		if (campaignState.activeQuests.isEmpty()) {
			io.println("I have no active quests... ");
		} else {
			io.println("Your current quests are: ");
			io.listIdentifiers(campaignState.activeQuests, ListType.NUMBERED);

			io.println("Enter a number to get more information, or type exit to back out");
			try {
				Quest selectedQuest = io.queryUserInputAgainstIdentifiers(campaignState.activeQuests,
						SelectionMethod.NUMBERED);

				io.listIdentifiers(selectedQuest.getObjectives(), ListType.BULLET);
			} catch (InterruptedQueryException e) {
				io.println("You awaken from your deep concentration");
			}
		}
	}

	public CampaignState getCampaignState() {
		return campaignState;
	}

	public static Campaign loadCampaignFromXML(File file) {
		return null;
	}

	public void loop() {
		io.println("What would you like to do? (Type help for options)");
		String input = io.queryUserInputAgainstStrings(options, SelectionMethod.TEXT);

		switch (input) {
		case "Look around":
			lookAround();
			break;
		case "Move to":
			moveTo();
			break;
		case "Talk":
			talk();
			break;
		case "View Quests":
			viewQuests();
			break;
		case "Help":
			io.listStrings(options, ListType.ONE_LINE);
			break;
		case "Exit":
			shouldExitCampaign = true;
			break;
		}
	}

	public boolean shouldExitCampaign() {
		return shouldExitCampaign;
	}

	public void start() {
		io.println(introduction);
	}
}
