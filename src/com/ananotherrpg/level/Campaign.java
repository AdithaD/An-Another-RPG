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

	private final ArrayList<String> options = new ArrayList<String>(
			Arrays.asList(new String[] { "Look around", "Move to", "Examine", "Talk", "View Quests", "View Inventory", "Pick Up", "Help" }));

	private int campaignId;
	private String introduction;
	
	private Map<Integer, Quest> questLookup;
	private List<Quest> activeQuests;

	private Graph<Location> locations;
	private Location currentLocation;

	private Player player;

	public boolean isComplete;
	private boolean shouldExitCampaign;

	public Campaign(int campaignId, String introduction, List<Quest> campaignQuests, Graph<Location> locations,
			Location currentLocation, List<Quest> activeQuests, Player player, boolean isComplete) {
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

	}
	
	public void loop() {
		IOManager.println("What would you like to do? (Type help for options)");
		Optional<String> opInput = IOManager.queryUserInputAgainstStrings(options, SelectionMethod.TEXT);

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
				case "View Inventory":
					player.viewInventory();
					break;
				case "Pick Up":
					pickUp();
					break;
				case "Examine":
					examine();
					break;
				case "Help":
					IOManager.listStrings(options, ListType.ONE_LINE);
					break;
			}
		} else {
			IOManager.println("Returning to main menu");
			shouldExitCampaign = true;
		}

	}

	public void start() {
		IOManager.println(introduction);
	}

	private void talk() {
		if (currentLocation.getPermanentEntities().isEmpty()) {
			IOManager.println("There is no one to talk to");
		} else {

			IOManager.println("Who would you like to talk to?");
			Optional<Entity> opEntity = IOManager.listAndQueryUserInputAgainstIdentifiers(
					currentLocation.getPermanentEntities(), ListType.NUMBERED, SelectionMethod.NUMBERED);

			if (opEntity.isPresent()) {
				DialogueManager manager = new DialogueManager(opEntity.get().getDialogueGraph());

				manager.initiateDialogue();

				manager.getNewQuests().forEach(id -> {
					activeQuests.add(questLookup.get(id));
				});
			} else {
				IOManager.println("You decide otherwise");
			}

		}
	}

	private void moveTo() {
		List<Location> adjacentLocations = locations.getAdjacentNodes(currentLocation);

		if (adjacentLocations.isEmpty()) {
			IOManager.println("There is no where to go to!");
		} else {
			IOManager.println("Where would you like to go ?");

			Optional<Location> opLocation = IOManager.listAndQueryUserInputAgainstIdentifiers(adjacentLocations,
					ListType.NUMBERED, SelectionMethod.NUMBERED);

			if (opLocation.isPresent()) {
				currentLocation = opLocation.get();
				IOManager.println("You move to " + currentLocation.getName());
			} else {
				IOManager.println("Staying here is fine for now...");
			}

		}

	}

	private void lookAround() {
		IOManager.println(currentLocation.getDescription());
		IOManager.println("You do a quick whirl and you see:");

		List<Entity> permanentEntities = currentLocation.getPermanentEntities();
		List<ItemStack> itemStacks = currentLocation.getItemStacks();
		List<Location> accessibleLocations = getAccessibleLocations();

		if (permanentEntities.isEmpty()) {
			IOManager.println("There is noone in this area");
		} else {
			IOManager.listIdentifiers(permanentEntities, ListType.BULLET);
		}

		if (itemStacks.isEmpty()) {
			IOManager.println("There is no items in this area");
		} else {
			IOManager.listIdentifiers(itemStacks, ListType.BULLET);
		}

		if (accessibleLocations.isEmpty()) {
			IOManager.println("I can't see anywhere to go right now");
		} else {
			IOManager.listIdentifiers(accessibleLocations, ListType.BULLET);
		}
	}

	private void examine(){

		List<? extends Identifiable> identifiables = List.of(currentLocation.getPermanentEntities(), currentLocation.getItemStacks(), getAccessibleLocations()).stream().flatMap(Collection::stream).collect(Collectors.toList());

		IOManager.println("What would you like to examine: (Type its name)");
		Optional<? extends Identifiable> opIdentifiable = IOManager.queryUserInputAgainstIdentifiers(identifiables, SelectionMethod.TEXT);

		if(opIdentifiable.isPresent()){
			IOManager.println(opIdentifiable.get().getDetails());
		}else{
			IOManager.println("You know enough already. Evidently...");
		}

	}

	private void viewQuests() {
		if (activeQuests.isEmpty()) {
			IOManager.println("I have no active quests... ");
		} else {
			IOManager.println("Your current quests are: ");
			IOManager.listStrings(activeQuests.stream().map(Quest::getName).collect(Collectors.toList()), ListType.NUMBERED);

			IOManager.println("Enter a number to get more information, or type exit to back out");

			Optional<Quest> opQuest = IOManager.queryUserInputAgainstCustomMap(activeQuests, Quest::getName, SelectionMethod.NUMBERED);

			if (opQuest.isPresent()) {
				Quest selectedQuest = opQuest.get();

				IOManager.listStrings(selectedQuest.getObjectives().stream().map(Objective::getDescription).collect(Collectors.toList()),ListType.BULLET);
			} else {
				IOManager.println("You awaken from your deep concentration");
			}
		}
	}

	public void pickUp() {
		Optional<ItemStack> opItem = IOManager.listAndQueryUserInputAgainstIdentifiers(currentLocation.getItemStacks(), ListType.NUMBERED, SelectionMethod.NUMBERED);

		if(opItem.isPresent()){
			player.inventory.addToInventory(opItem.get());
		}else{
			IOManager.println("Your rucksack is doesn't need another item, you say.");
		}
	}
	
	public boolean shouldExitCampaign() {
		return shouldExitCampaign;
	}

	private List<Location> getAccessibleLocations() {
		return locations.getAdjacentNodes(currentLocation);
	}

	public static Campaign loadCampaignFromXML(File file) {
		return null;
	}
}
