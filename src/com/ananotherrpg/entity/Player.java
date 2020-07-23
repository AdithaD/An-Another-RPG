package com.ananotherrpg.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.Campaign;
/**
 * The Player class is responsible for the main game loop, and user interaction with the game
 */
public class Player {

	private final ArrayList<String> options = new ArrayList<String>(Arrays.asList(new String[] { "Look around",
			"Move to", "Examine", "Talk", "View Quests", "View Inventory", "Pick Up", "Help" }));


	private boolean shouldExitCampaign = false;

	private Entity playerEntity;
	private int xp;

	private List<Integer> knownLocations;
	private List<Integer> activeQuests;

	public void talk() {
		// if (locationManager.getPermanentEntitiesInCurrentLocation().isEmpty()) {
		// 	IOManager.println("There is no one to talk to");
		// } else {

		// 	IOManager.println("Who would you like to talk to?");
		// 	Optional<Entity> opEntity = IOManager.listAndQueryUserInputAgainstIdentifiers(
		// 			locationManager.getPermanentEntitiesInCurrentLocation(), ListType.NUMBERED,
		// 			SelectionMethod.NUMBERED);

		// 	if (opEntity.isPresent()) {
		// 		dialogueManager.initiateDialogue(opEntity.get());

		// 		questManager.addNewQuestsById(dialogueManager.getNewQuests());
		// 	} else {
		// 		IOManager.println("You decide otherwise");
		// 	}

		// }
	}

	public void moveTo() {

		// if (getAccessibleLocations().isEmpty()) {
		// 	IOManager.println("There is no where to go to!");
		// } else {
		// 	IOManager.println("Where would you like to go ?");

		// 	Optional<Location> opLocation = IOManager.listAndQueryUserInputAgainstIdentifiers(getAccessibleLocations(),
		// 			ListType.NUMBERED, SelectionMethod.NUMBERED);

		// 	if (opLocation.isPresent()) {
		// 		currentLocation = opLocation.get();
		// 		IOManager.println("You move to " + getCurrentLocation().getName());
		// 	} else {
		// 		IOManager.println("Staying here is fine for now...");
		// 	}

		// }

	}

	public void lookAround() {
		// currentLocation.printLocationDetails();

		// if (getAccessibleLocations().isEmpty()) {
		// 	IOManager.println("I can't see anywhere to go right now");
		// } else {
		// 	IOManager.listIdentifiers(getAccessibleLocations(), ListType.BULLET);
		// }
	}

	public void examine() {
		// List<? extends Identifiable> identifiables = locationManager.getListOfIdentifiablesInCurrentLocation();

		// IOManager.println("What would you like to examine: (Type its name)");
		// Optional<? extends Identifiable> opIdentifiable = IOManager.queryUserInputAgainstIdentifiers(identifiables,
		// 		SelectionMethod.TEXT);

		// if (opIdentifiable.isPresent()) {
		// 	IOManager.println(opIdentifiable.get().getDetails());
		// } else {
		// 	IOManager.println("You know enough already. Evidently...");
		// }
	}

	public void pickUp() {
		// Optional<ItemStack> opItem = IOManager.listAndQueryUserInputAgainstIdentifiers(
		// 		locationManager.getCurrentLocation().getItemStacks(), ListType.NUMBERED, SelectionMethod.NUMBERED);

		// if (opItem.isPresent()) {
		// 	inventory.addToInventory(opItem.get());
		// } else {
		// 	IOManager.println("Your rucksack is doesn't need another item, you say.");
		// }
	}

	public void viewInventory() {
		// IOManager.println("Your inventory contains: ");
		// IOManager.listIdentifiers(inventory.getItemStacks(), ListType.NUMBERED);

		// IOManager.println("Enter a number to interact with that Item");
		// Optional<ItemStack> opItemStack = IOManager.queryUserInputAgainstIdentifiers(inventory.getItemStacks(),
		// 		SelectionMethod.NUMBERED);

		// if (opItemStack.isPresent()) {
		// 	opItemStack.get().getItem().use(this);
		// } else {

		// }

	}

	private void viewQuests() {
		// if (activeQuests.isEmpty()) {
		// 	IOManager.println("I have no active quests... ");
		// } else {
		// 	IOManager.println("Your current quests are: ");
		// 	IOManager.listStrings(activeQuests.stream().map(Quest::getName).collect(Collectors.toList()),
		// 			ListType.NUMBERED);

		// 	IOManager.println("Enter a number to get more information, or type exit to back out");

		// 	Optional<Quest> opQuest = IOManager.queryUserInputAgainstCustomMap(activeQuests, Quest::getName,
		// 			SelectionMethod.NUMBERED);

		// 	if (opQuest.isPresent()) {
		// 		Quest selectedQuest = opQuest.get();

		// 		IOManager.listStrings(selectedQuest.getObjectives().stream().map(Objective::getDescription)
		// 				.collect(Collectors.toList()), ListType.BULLET);
		// 	} else {
		// 		IOManager.println("You awaken from your deep concentration");
		// 	}
		// }
	}

	public void play(Campaign currentCampaign) {
		while (!shouldExitCampaign) {
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
						viewInventory();
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

	}

	public Entity getEntity() {
		return playerEntity;
	}

}
