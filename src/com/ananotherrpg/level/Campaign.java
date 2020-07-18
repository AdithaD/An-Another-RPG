package com.ananotherrpg.level;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Optional;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.dialogue.DialogueLine;
import com.ananotherrpg.entity.dialogue.DialogueManager;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
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

			io.println("Who would you like to talk to?");
			Optional<Entity> opEntity = io.listAndQueryUserInputAgainstIdentifiers(
					campaignState.currentLocation.getPermanentEntities(), ListType.NUMBERED, SelectionMethod.NUMBERED);

			if (opEntity.isPresent()) {
				DialogueManager manager = new DialogueManager(opEntity.get().getDialogueGraph(), io);

				manager.initiateDialogue();

				manager.getNewQuests().forEach(id -> {
					campaignState.activeQuests.add(questLookup.get(id));
				});
			} else {
				io.println("You decide otherwise");
			}

		}
	}

	private void moveTo() {
		List<Location> adjacentLocations = campaignState.locations.getAdjacentNodes(campaignState.currentLocation);

		if (adjacentLocations.isEmpty()) {
			io.println("There is no where to go to!");
		} else {
			io.println("Where would you like to go ?");

			Optional<Location> opLocation = io.listAndQueryUserInputAgainstIdentifiers(adjacentLocations,
					ListType.NUMBERED, SelectionMethod.NUMBERED);

			if (opLocation.isPresent()) {
				campaignState.setCurrentLocation(opLocation.get());
				io.println("You move to " + campaignState.currentLocation.getName());
			} else {
				io.println("Staying here is fine for now...");
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

			Optional<Quest> opQuest = io.queryUserInputAgainstIdentifiers(campaignState.activeQuests,
					SelectionMethod.NUMBERED);

			if (opQuest.isPresent()) {
				Quest selectedQuest = opQuest.get();

				io.listIdentifiers(selectedQuest.getObjectives(), ListType.BULLET);
			} else {
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
		Optional<String> opInput = io.queryUserInputAgainstStrings(options, SelectionMethod.TEXT);

		if (opInput.isPresent()) {
			switch (opInput.get()) {
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
			}
		} else {
			io.println("Returning to main menu");
			shouldExitCampaign = true;
		}

	}

	public boolean shouldExitCampaign() {
		return shouldExitCampaign;
	}

	public void start() {
		io.println(introduction);
	}
}
