package com.ananotherrpg;

import java.util.ArrayList;
import java.util.Arrays;

import com.ananotherrpg.entity.Player;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.Campaign;

/**
 * The Game class is reponsible for loading or creating new Campaigns, and providing them to Player object to play.
 */
public class Game {

	private Campaign currentCampaign;
	private Player player;

	private enum GameState {
		MENU, CAMPAIGN
	}

	private GameState gameState = GameState.MENU;

	private static Boolean shouldExit = false;

	public Game() {

		// // Object construction order should be: 1. (ItemStack, DialogueGraph) 2.
		// // (Inventory) 3. (Entity) 4. (Location) 5.(Quest, LocationGraph) 6.(Campaign)

		// int campaignID = 1;

		// // 1. Items / ItemStacks
		// Item key = new Item("Key");
		// Weapon sword = new Weapon("Mjollnir", 3, 0.2, 2);

		// // 1 .DialogueGraph
		// DialogueLine managerDialogueLine1 = new DialogueLine("Hello! Are you the adventurer I asked for?");
		// DialogueLine managerDialogueLine2 = new QuestDialogueLine(
		// 		"Wow! There's a room over there with a scary old man! Clear him out for me. One way or the other...",
		// 		1);
		// DialogueLine managerDialogueLine3 = new DialogueLine("Well, that's certainly a disappointment");

		// DirectedDataLink<DialogueLine, String> managerDialogueLink12 = new DirectedDataLink<DialogueLine, String>(
		// 		managerDialogueLine2, "I'm much more than that. I'm the best in the business!");
		// DirectedDataLink<DialogueLine, String> managerDialogueLink122 = new DirectedDataLink<DialogueLine, String>(
		// 		managerDialogueLine2, "Yes.");
		// DirectedDataLink<DialogueLine, String> managerDialogueLink13 = new DirectedDataLink<DialogueLine, String>(
		// 		managerDialogueLine3, "No, I think you've mistaken me for someone.");

		// DirectedDataGraph<DialogueLine, String> managerDialogueGraph = new DirectedDataGraph<DialogueLine, String>(
		// 		managerDialogueLine1);
		// managerDialogueGraph.addNode(managerDialogueLine1);
		// managerDialogueGraph.addNode(managerDialogueLine2);
		// managerDialogueGraph.addNode(managerDialogueLine3);

		// managerDialogueGraph.addLink(managerDialogueLine1, managerDialogueLink12);
		// managerDialogueGraph.addLink(managerDialogueLine1, managerDialogueLink122);
		// managerDialogueGraph.addLink(managerDialogueLine1, managerDialogueLink13);

		// // 2. Inventory

		// // 3. Entities
		// Entity manager = new Entity("Manager", 20, 20, new Inventory(), false, true, managerDialogueGraph);
		// Combatant angryman = new Combatant("Angry Man", 20, 20, new Inventory(), 4, 1, Weapon.UNARMED, false);

		// // 4. Location
		// ArrayList<Entity> lobbyEntities = new ArrayList<Entity>();
		// lobbyEntities.add(manager);

		// ArrayList<ItemStack> lobbyItems = new ArrayList<ItemStack>();
		// lobbyItems.add(new ItemStack(sword, 1));

		// Location lobby = new Location("lobby", "An illustrious hotel lobby filled with an air of richness.",
		// 		lobbyEntities, lobbyItems);

		// ArrayList<ItemStack> room1Items = new ArrayList<ItemStack>();
		// room1Items.add(new ItemStack(sword, 1));
		// room1Items.add(new ItemStack(key, 1));

		// ArrayList<Entity> room1Entities = new ArrayList<Entity>();
		// room1Entities.add(angryman);

		// Location room1 = new Location("A Non-descript room",
		// 		"A dainty, run-down room who's atmosphere is almost unbecoming of the hotel it's connected to",
		// 		room1Entities, room1Items);

		// // 5. LocationGraph
		// Graph<Location> locations = new Graph<Location>();

		// locations.addNode(lobby);
		// locations.addNode(room1);

		// locations.addLink(lobby, room1, false);

		// // 5. Quests
		// ArrayList<Combatant> killTargets = new ArrayList<Combatant>();
		// killTargets.add(angryman);

		// Objective killAngryMan = new KillObjective(11, "Kill the angry man", room1, killTargets);
		// List<Objective> objectives = new ArrayList<Objective>();
		// objectives.add(killAngryMan);

		// Quest killAngryBby = new Quest(1, "Save the hotel", objectives, false);

		// List<Quest> campaignQuests = new ArrayList<Quest>();
		// campaignQuests.add(killAngryBby);

		// Player player = new Player("Kim Tan", 100, 100, new Inventory(), 5, 1, 0);

		// // 6. Campaign
		// Campaign testCampaign = new Campaign(campaignID,
		// 		"A beautiful hotel lobby with an inconspicuous room to the side appears before you", campaignQuests,
		// 		locations, lobby, player, false);

		// currentCampaign = testCampaign;
	}

	public static void main(String[] args) {

		while (!shouldExit) {
			IOManager.println("Welcome to an another rpg!");

			ArrayList<String> options = new ArrayList<String>(
					Arrays.asList("Create a new campaign", "Load a previous campaign", "Quit :("));

			String input = IOManager.listAndQueryUserInputAgainstStringsWithoutExit(options, ListType.NUMBERED,
					SelectionMethod.NUMBERED);

			Game game;
			if (input == options.get(0)) {
				game = new Game();
				game.start();
			} else if (input == options.get(1)) {
				IOManager.print("NOT IMPLEMENTED");

			} else if (input == options.get(2)) {
				IOManager.print("Goodbye");
				shouldExit = true;
			}
		}

		System.exit(0);
	}

	private void start() {
		gameState = GameState.CAMPAIGN;

		player.play(currentCampaign);
	}

}
