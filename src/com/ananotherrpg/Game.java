package com.ananotherrpg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Combatant;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.dialogue.DialogueLine;
import com.ananotherrpg.entity.dialogue.DialogueManager;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.inventory.Weapon;
import com.ananotherrpg.level.Campaign;
import com.ananotherrpg.level.KillObjective;
import com.ananotherrpg.level.Location;
import com.ananotherrpg.level.Objective;
import com.ananotherrpg.level.Quest;
import com.ananotherrpg.util.Graph;
import com.ananotherrpg.util.Link;
import com.ananotherrpg.util.LinkedDirectedGraph;

public class Game {

	private Campaign campaign;

	private static Scanner s;

	private ArrayList<String> options = new ArrayList<String>(
			Arrays.asList(new String[] { "Look around", "Move to", "Examine", "Talk" }));

	private enum State {
		MENU, GAME, COMBAT
	}

	private State gameState = State.MENU;

	private Boolean shouldKeepPlaying = false;

	public Game() {

		int campaignID = 1;

		Weapon sword = new Weapon("Mjollnir", 3, 0.2, 2);
		ArrayList<ItemStack> lobbyItems = new ArrayList<ItemStack>();
		lobbyItems.add(new ItemStack(sword, 1));

		DialogueLine managerDialogueLine1 = new DialogueLine("Hello! Are you the adventurer I asked for?");
		DialogueLine managerDialogueLine2 = new DialogueLine(
				"Wow! There's a room over there with a scary old man! Clear him out for me. One way or the other...");
		DialogueLine managerDialogueLine3 = new DialogueLine("Well, that's certainly a disappointment");

		Link<DialogueLine, String> managerDialogueLink12 = new Link<DialogueLine, String>(managerDialogueLine2,
				"I'm much more than that. I'm the best in the business!");
		Link<DialogueLine, String> managerDialogueLink122 = new Link<DialogueLine, String>(managerDialogueLine2,
				"Yes.");
		Link<DialogueLine, String> managerDialogueLink13 = new Link<DialogueLine, String>(managerDialogueLine3,
				"No, I think you've mistaken me for someone.");

		LinkedDirectedGraph<DialogueLine, String> managerDialogueGraph = new LinkedDirectedGraph<DialogueLine, String>(
				managerDialogueLine1);
		managerDialogueGraph.addNode(managerDialogueLine1);
		managerDialogueGraph.addNode(managerDialogueLine2);
		managerDialogueGraph.addNode(managerDialogueLine3);

		managerDialogueGraph.addLink(managerDialogueLine1, managerDialogueLink12);
		managerDialogueGraph.addLink(managerDialogueLine1, managerDialogueLink122);
		managerDialogueGraph.addLink(managerDialogueLine1, managerDialogueLink13);

		Entity manager = new Entity("Manager", 20, 20, new Inventory(), false, true, managerDialogueGraph);
		ArrayList<Entity> lobbyEntities = new ArrayList<Entity>();
		lobbyEntities.add(manager);

		Location lobby = new Location("lobby", "An illustrious hotel lobby filled with an air of richness.",
				lobbyEntities, lobbyItems, true);

		Item key = new Item("Key");
		ArrayList<ItemStack> room1Items = new ArrayList<ItemStack>();
		room1Items.add(new ItemStack(sword, 1));
		room1Items.add(new ItemStack(key, 1));

		Combatant angryman = new Combatant("Angry Man", 20, 20, new Inventory(), 4, 1, Weapon.UNARMED, false);
		ArrayList<Entity> room1Entities = new ArrayList<Entity>();
		room1Entities.add(angryman);

		Location room1 = new Location("A Non-descript room",
				"A dainty, run-down room who's atmosphere is almost unbecoming of the hotel it's connected to",
				room1Entities, room1Items);

		Graph<Location> locations = new Graph<Location>();

		locations.addNode(lobby);
		locations.addNode(room1);

		locations.addEdge(lobby, room1);

		ArrayList<Combatant> killTargets = new ArrayList<Combatant>();
		killTargets.add(angryman);

		Objective killAngryMan = new KillObjective(1, "Kill the angry man", room1, killTargets);
		Objective[] objectives = { killAngryMan };

		Quest killAngryBby = new Quest(22, objectives, new Quest[0], false, false);

		Campaign testCampaign = new Campaign(campaignID,
				"A beautiful hotel lobby with an inconspicuous room to the side appears before you", locations, lobby,
				killAngryBby);

		campaign = testCampaign;

	}

	public Game(Campaign campaign) {

	}

	public static void main(String[] args) {

		s = new Scanner(System.in);

		System.out.println("Welcome to an another rpg!");
		System.out.println("Would you like to: \n 1 | Start a new campaign 2 | Load a previous campaign 3 | Quit ");

		int input = -1;

		while (!(input == 1 || input == 2 || input == 3)) {
			try {
				input = Integer.parseInt(s.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("That's not a valid option!");
			}
		}

		Game game;

		if (input == 3) {
			System.exit(0);
		} else {
			switch (input) {
				case 1:
					game = new Game();
					game.start();
					break;

				case 2:
					game = new Game(chooseSave());
					game.start();
					break;
			}
		}

	}

	private void start() {
		shouldKeepPlaying = true;
		gameState = State.GAME;

		System.out.println(campaign.getIntroduction());
		while (shouldKeepPlaying) {
			if (gameState == State.GAME) {
				System.out.println("What would you like to do?");

				String input = s.nextLine();

				while (!options.contains(input)) {
					System.out.println("That's not a valid option");
					input = s.nextLine();
				}

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
				}
			}
		}
	}

	private void talk() {
		System.out.println("Who would you like to talk to?");

		listIdentifiers(campaign.getCurrentLocation().getPermanentEntities());

		Entity target = queryUserInputAgainstIdentifiers(campaign.getCurrentLocation().getPermanentEntities(), s);

		initiateDialogue(target);
	}

	private void initiateDialogue(Entity target) {
		DialogueManager manager = new DialogueManager(target.getDialogueGraph());

		System.out.println(manager.getDialogue());
		while (manager.hasMoreDialogue()) {
			Map<String, Link<DialogueLine, String>> linkToLinkDataMap = manager.generateLinkToLinkDataMap();
			List<String> optionsText = new ArrayList<String>(linkToLinkDataMap.keySet());

			listStrings(optionsText);

			manager.traverseLink(linkToLinkDataMap.get(queryUserAgainstStrings(optionsText, s)));
			System.out.println(manager.getDialogue());
		}

		campaign.getQuestsLog().addAll(manager.getNewQuests());
	}

	private void moveTo() {
		System.out.println("Where would you like to go ?");

		List<Location> adjacentLocations = campaign.getLocationGraph().getAdjacentNodes(campaign.getCurrentLocation());

		listIdentifiers(adjacentLocations);

		campaign.setCurrentLocation(queryUserInputAgainstIdentifiers(adjacentLocations, s));
		System.out.println("You move to " + campaign.getCurrentLocation().getName());

	}

	private void lookAround() {
		System.out.println(campaign.getCurrentLocation().getDescription());
		System.out.println("You do a quick whirl and you see:");

		listIdentifiers(campaign.getCurrentLocation().getPermanentEntities());

		listIdentifiers(campaign.getCurrentLocation().getItemStacks());

		listIdentifiers(campaign.getLocationGraph().getAdjacentNodes(campaign.getCurrentLocation()));
	}

	private <T extends Identifiable> T queryUserInputAgainstIdentifiers(List<T> data, Scanner s) {

		String input = s.nextLine();

		Map<String, T> options = data.stream().collect(Collectors.toMap(T::getName, Function.identity()));

		while (!options.keySet().contains(input)) {
			System.out.println("That's not a valid option");
			input = s.nextLine();
		}

		return options.get(input);
	}

	private void listIdentifiers(List<? extends Identifiable> list) {
		for (Identifiable object : list) {
			if (object.isKnown()) {
				System.out.println("* " + object.getName());
			}
		}
	}

	private String queryUserAgainstStrings(List<String> data, Scanner s) {

		System.out.print("Enter choice number: ");

		String[] intToStringMap = data.toArray(new String[0]);

		int input = -1;

		while (!(input >= 1) && (input <= intToStringMap.length)) {
			try {
				input = Integer.parseInt(s.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("That's not a valid option!");
			}
		}
		return intToStringMap[input - 1];
	}

	private void listStrings(List<String> data) {
		for (int i = 0; i < data.size(); i++) {
			System.out.println((i + 1) + ". " + data.get(i));
		}
	}

	private static Campaign chooseSave() {
		return null;

	}

}
