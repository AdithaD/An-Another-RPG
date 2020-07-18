package com.ananotherrpg.level;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.Player;
import com.ananotherrpg.entity.dialogue.DialogueManager;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.util.Graph;

public class Campaign {

	private ArrayList<String> options = new ArrayList<String>(
			Arrays.asList(new String[] { "Look around", "Move to", "Examine", "Talk", "View Quests", "Help" }));

	private int campaignId;
	private String introduction;
	private Map<Integer, Quest> questLookup;

	private Graph<Location> locations;
	private Location currentLocation;

	private List<Quest> activeQuests;

	private Player player;

	public boolean isComplete;

	private IOManager io;
	private boolean shouldExitCampaign;

	public Campaign(int campaignId, String introduction, List<Quest> campaignQuests, Graph<Location> locations,
			Location currentLocation, List<Quest> activeQuests, Player player, boolean isComplete, IOManager io) {
		super();
		this.introduction = introduction;
		this.campaignId = campaignId;
		this.questLookup = campaignQuests.stream().collect(Collectors.toMap(Quest::getId, Function.identity()));
		this.shouldExitCampaign = false;

		this.locations = locations;
		this.currentLocation = currentLocation;
		this.activeQuests = activeQuests;
		this.player = player;
		this.isComplete = isComplete;

		this.io = io;

	}

	private void talk() {
		if (currentLocation.getPermanentEntities().isEmpty()) {
			io.println("There is no one to talk to");
		} else {

			io.println("Who would you like to talk to?");
			Optional<Entity> opEntity = io.listAndQueryUserInputAgainstIdentifiers(
					currentLocation.getPermanentEntities(), ListType.NUMBERED, SelectionMethod.NUMBERED);

			if (opEntity.isPresent()) {
				DialogueManager manager = new DialogueManager(opEntity.get().getDialogueGraph(), io);

				manager.initiateDialogue();

				manager.getNewQuests().forEach(id -> {
					activeQuests.add(questLookup.get(id));
				});
			} else {
				io.println("You decide otherwise");
			}

		}
	}

	private void moveTo() {
		List<Location> adjacentLocations = locations.getAdjacentNodes(currentLocation);

		if (adjacentLocations.isEmpty()) {
			io.println("There is no where to go to!");
		} else {
			io.println("Where would you like to go ?");

			Optional<Location> opLocation = io.listAndQueryUserInputAgainstIdentifiers(adjacentLocations,
					ListType.NUMBERED, SelectionMethod.NUMBERED);

			if (opLocation.isPresent()) {
				currentLocation = opLocation.get();
				io.println("You move to " + currentLocation.getName());
			} else {
				io.println("Staying here is fine for now...");
			}

		}

	}

	private void lookAround() {
		io.println(currentLocation.getDescription());
		io.println("You do a quick whirl and you see:");

		List<Entity> permanentEntities = currentLocation.getPermanentEntities();
		List<ItemStack> itemStacks = currentLocation.getItemStacks();
		List<Location> accessibleLocations = getAccessibleLocations();

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
		if (activeQuests.isEmpty()) {
			io.println("I have no active quests... ");
		} else {
			io.println("Your current quests are: ");
			io.listIdentifiers(activeQuests, ListType.NUMBERED);

			io.println("Enter a number to get more information, or type exit to back out");

			Optional<Quest> opQuest = io.queryUserInputAgainstIdentifiers(activeQuests, SelectionMethod.NUMBERED);

			if (opQuest.isPresent()) {
				Quest selectedQuest = opQuest.get();

				io.listIdentifiers(selectedQuest.getObjectives(), ListType.BULLET);
			} else {
				io.println("You awaken from your deep concentration");
			}
		}
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

	public void start() {
		io.println(introduction);
	}

	public boolean shouldExitCampaign() {
		return shouldExitCampaign;
	}

	private List<Location> getAccessibleLocations() {
		return locations.getAdjacentNodes(currentLocation);
	}
}
