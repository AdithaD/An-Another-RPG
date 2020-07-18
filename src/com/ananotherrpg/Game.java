package com.ananotherrpg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ananotherrpg.entity.Combatant;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.Player;
import com.ananotherrpg.entity.dialogue.DialogueLine;
import com.ananotherrpg.entity.dialogue.QuestDialogueLine;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.inventory.Weapon;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.Campaign;
import com.ananotherrpg.level.CampaignState;
import com.ananotherrpg.level.KillObjective;
import com.ananotherrpg.level.Location;
import com.ananotherrpg.level.Objective;
import com.ananotherrpg.level.Quest;
import com.ananotherrpg.util.Graph;
import com.ananotherrpg.util.Link;
import com.ananotherrpg.util.LinkedDirectedGraph;

public class Game {

	private Campaign campaign;

	private static IOManager io;

	private enum State {
		MENU, GAME, COMBAT
	}

	private State gameState = State.MENU;

	private static Boolean shouldExit = false;

	public Game() {

		// Object construction order should be: 1. (ItemStack, DialogueGraph) 2.
		// (Inventory) 3. (Entity) 4. (Location) 5.(Quest, LocationGraph) 6.(Campaign)

		int campaignID = 1;

		// 1. Items / ItemStacks
		Item key = new Item("Key");
		Weapon sword = new Weapon("Mjollnir", 3, 0.2, 2);

		// 1 .DialogueGraph
		DialogueLine managerDialogueLine1 = new DialogueLine("Hello! Are you the adventurer I asked for?");
		DialogueLine managerDialogueLine2 = new QuestDialogueLine(
				"Wow! There's a room over there with a scary old man! Clear him out for me. One way or the other...", 1);
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

		// 2. Inventory

		// 3. Entities
		Entity manager = new Entity("Manager", 20, 20, new Inventory(), false, true, managerDialogueGraph);
		Combatant angryman = new Combatant("Angry Man", 20, 20, new Inventory(), 4, 1, Weapon.UNARMED, false);

		// 4. Location
		ArrayList<Entity> lobbyEntities = new ArrayList<Entity>();
		lobbyEntities.add(manager);

		ArrayList<ItemStack> lobbyItems = new ArrayList<ItemStack>();
		lobbyItems.add(new ItemStack(sword, 1));

		Location lobby = new Location("lobby", "An illustrious hotel lobby filled with an air of richness.",
				lobbyEntities, lobbyItems, true);

		ArrayList<ItemStack> room1Items = new ArrayList<ItemStack>();
		room1Items.add(new ItemStack(sword, 1));
		room1Items.add(new ItemStack(key, 1));

		ArrayList<Entity> room1Entities = new ArrayList<Entity>();
		room1Entities.add(angryman);

		Location room1 = new Location("A Non-descript room",
				"A dainty, run-down room who's atmosphere is almost unbecoming of the hotel it's connected to",
				room1Entities, room1Items);

		// 5. LocationGraph
		Graph<Location> locations = new Graph<Location>();

		locations.addNode(lobby);
		locations.addNode(room1);

		locations.addEdge(lobby, room1);

		// 5. Quests
		ArrayList<Combatant> killTargets = new ArrayList<Combatant>();
		killTargets.add(angryman);

		Objective killAngryMan = new KillObjective(11, "Kill the angry man", room1, killTargets);
		List<Objective> objectives = new ArrayList<Objective>();
		objectives.add(killAngryMan);

		Quest killAngryBby = new Quest(1, "Save the hotel", objectives, false);

		List<Quest> campaignQuests = new ArrayList<Quest>();
		campaignQuests.add(killAngryBby);

		CampaignState campaignState = new CampaignState(locations, lobby, new ArrayList<Quest>(),
				new Player("Mark", 20, 20, new Inventory(), 5, 1), false);

		// 6. Campaign
		Campaign testCampaign = new Campaign(campaignID,
				"A beautiful hotel lobby with an inconspicuous room to the side appears before you", campaignQuests,
				campaignState, io);

		campaign = testCampaign;
	}

	public Game(Campaign campaign) {

	}

	public static void main(String[] args) {
		io = new IOManager();

		while (!shouldExit) {
			gotoMainMenu();
		}

		System.exit(0);
	}

	private static void gotoMainMenu() {
		io.println("Welcome to an another rpg!");

		ArrayList<String> options = new ArrayList<String>(
				Arrays.asList("Create a new campaign", "Load a previous campaign", "Quit :("));

		String input = io.listAndQueryUserInputAgainstStringsWithoutExit(options, ListType.NUMBERED, SelectionMethod.NUMBERED);

		Game game;
		if (input == options.get(0)) {
			game = new Game();
			game.start();
		} else if (input == options.get(1)) {
			io.print("NOT IMPLEMENTED");

		} else if (input == options.get(2)) {
			io.print("Goodbye");
			shouldExit = true;
		}
	}

	private void start() {
		gameState = State.GAME;

		campaign.start();
		while (gameState == State.GAME) {
			campaign.loop();

			if (campaign.shouldExitCampaign()) {
				gameState = State.MENU;
			}
		}
	}

	private static Campaign chooseSave() {
		return null;

	}

}
