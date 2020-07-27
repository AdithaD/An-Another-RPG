package com.ananotherrpg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

import com.ananotherrpg.entity.Attributes;
import com.ananotherrpg.entity.Attributes.Attribute;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.PlayerAvatar;
import com.ananotherrpg.entity.QuestLog;
import com.ananotherrpg.entity.dialogue.Dialogue;
import com.ananotherrpg.entity.dialogue.DialogueLine;
import com.ananotherrpg.entity.dialogue.PathDialogueLine;
import com.ananotherrpg.entity.dialogue.QuestDialogueLine;
import com.ananotherrpg.entity.dialogue.Response;
import com.ananotherrpg.event.EventDispatcher.GameEvent;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.Weapon;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.CampaignData;
import com.ananotherrpg.level.CampaignState;
import com.ananotherrpg.level.EntityTemplate;
import com.ananotherrpg.level.Location;
import com.ananotherrpg.level.LocationGraph;
import com.ananotherrpg.level.Objective;
import com.ananotherrpg.level.Path;
import com.ananotherrpg.level.QuestTemplate;
import com.ananotherrpg.level.TallyObjective;
import com.ananotherrpg.util.DirectedGraph;

/**
 * The Game class is reponsible for loading or creating new Campaigns, and
 * providing them to Player object to play.
 */
public class Game {

	private Campaign currentCampaign;
	private PlayerAvatar player;

	private enum GameState {
		MENU, CAMPAIGN
	}

	private GameState gameState = GameState.MENU;

	private static Boolean shouldExit = false;

	public static void main(String[] args) {

		while (!shouldExit) {
			IOManager.println("Welcome to an another rpg!");

			ArrayList<String> options = new ArrayList<String>(
					Arrays.asList("Create a new campaign", "Load a previous campaign", "Quit :("));

			Optional<String> opInput = IOManager.listAndQueryUserInputAgainstStrings(options, ListType.NUMBERED,
					SelectionMethod.NUMBERED, false);

			String input;
			if (opInput.isPresent()) {
				input = opInput.get();
			} else {
				throw new IllegalStateException("User didn't choose an option!");
			}

			Game game;
			if (input == options.get(0)) {
				game = testGame();
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

		currentCampaign.play();
	}

	public static Game testGame() {

		// Object construction order should be: 1. (ItemStack, DialogueGraph) 2.
		// (Inventory) 3. (Entity) 4. (Location) 5.(Quest, LocationGraph) 6.(Campaign)

		// 1. Items / ItemStacks
		Item key = new Item(0000, "Key", "A rusty old key", 1, 10, false);
		Item reward = new Item(0001, "Reward", "This is all you could ever ask for!", 5, 1000, false);
		Weapon mjollnir = new Weapon(0101, "Mjollnir", "The hammer of legend", 20, 150, 3, 0.2, 2);
		Weapon rabidClaws = new Weapon(0102, "Rabid Claws", "Biological warfare.", 0, 0, 4, 0.5, 1.5);

		// Quests / Objectives
		Objective killAngryManObjective = new TallyObjective("Kill the Angry Man", 1002, GameEvent.KILL, 1);
		List<Objective> objectives = new ArrayList<Objective>();
		objectives.add(killAngryManObjective);

		QuestTemplate killAngryBby = new QuestTemplate(3000, "Save the hotel", "A quest of upmost importance",
				objectives);
		List<QuestTemplate> campaignQuests = new ArrayList<QuestTemplate>();
		campaignQuests.add(killAngryBby);

		// 1 .Dialogue
		DialogueLine managerDialogueLine1 = new DialogueLine("Hello! Are you the adventurer I asked for?");
		DialogueLine managerDialogueLine2 = new DialogueLine("Well, that's certainly a disappointment");
		DialogueLine managerDialogueLine3 = new PathDialogueLine(
				"Okay, in that room over there, there's a scary old man who refuses to leave.", 7001);
		DialogueLine managerDialogueLine4 = new DialogueLine("I want you to get rid of him. Quickly. Will you do it?");
		DialogueLine managerDialogueLine5 = new QuestDialogueLine("Great! You will rewarded handsomely.", killAngryBby);

		Response playerResponse13 = new Response(managerDialogueLine3,
				"I'm much more than that. I'm the best in the business!");
		Response playerResponse132 = new Response(managerDialogueLine3, "Yes.");
		Response playerResponse12 = new Response(managerDialogueLine2, "No, I think you've mistaken me for someone.");

		Response playerResponse34 = new Response(managerDialogueLine4, "And?");
		Response playerResponse342 = new Response(managerDialogueLine4, "Don't tell me...");

		Response playerResponse42 = new Response(managerDialogueLine2, "I will do no such thing!");
		Response playerResponse45 = new Response(managerDialogueLine5, "Sure! Time for him to meet the grim reaper!");

		DirectedGraph<DialogueLine, Response> managerDialogueGraph = new DirectedGraph<DialogueLine, Response>();

		managerDialogueGraph.addNode(managerDialogueLine1);
		managerDialogueGraph.addNode(managerDialogueLine2);
		managerDialogueGraph.addNode(managerDialogueLine3);
		managerDialogueGraph.addNode(managerDialogueLine4);
		managerDialogueGraph.addNode(managerDialogueLine5);

		managerDialogueGraph.addLink(managerDialogueLine1, playerResponse13);
		managerDialogueGraph.addLink(managerDialogueLine1, playerResponse132);
		managerDialogueGraph.addLink(managerDialogueLine1, playerResponse12);

		managerDialogueGraph.addLink(managerDialogueLine3, playerResponse34);
		managerDialogueGraph.addLink(managerDialogueLine3, playerResponse342);

		managerDialogueGraph.addLink(managerDialogueLine4, playerResponse42);
		managerDialogueGraph.addLink(managerDialogueLine4, playerResponse45);

		Dialogue managerDialogue = new Dialogue(managerDialogueGraph, managerDialogueLine1);

		// 2. Entity Inventory

		// 3. Entities
		Entity manager = new Entity(1001, "Manager", "He's just doing his job...", new Attributes(1, 1, 5, 3), 1,
				new Inventory(), managerDialogue, true);

		Inventory angrymanInv = new Inventory();
		angrymanInv.equipWeapon(rabidClaws);
		Entity angryman = new Entity(1002, "Angry Man", "He's VERY angry. And old.", new Attributes(5, 3, 4, 0), 1,
				angrymanInv, Dialogue.NO_DIALOGUE, false);

		EnumMap<Attribute, Integer> attributeDistribution = new EnumMap<>(Attribute.class);
		attributeDistribution.put(Attribute.STRENGTH, 4);
		attributeDistribution.put(Attribute.AGILITY, 3);
		attributeDistribution.put(Attribute.CONSTITUTION, 3);
		attributeDistribution.put(Attribute.CHARISMA, 0);

		EntityTemplate zombie = new EntityTemplate(1100, "Zombie", "He's definitely rating this place 1-star!",
				Weapon.FISTS, new ArrayList<Item>(), Dialogue.NO_DIALOGUE, attributeDistribution);

		// 4. Location
		Inventory lobbyItemsOnGround = new Inventory();
		lobbyItemsOnGround.addToInventory(key, 1);
		lobbyItemsOnGround.addToInventory(mjollnir, 1);

		ArrayList<Entity> lobbyEntities = new ArrayList<Entity>();
		lobbyEntities.add(manager);

		Location lobby = new Location(2000, "lobby", "An illustrious hotel lobby filled with an air of richness.",
				lobbyEntities, lobbyItemsOnGround);

		Inventory room1ItemsOnGround = new Inventory();

		ArrayList<Entity> room1Entities = new ArrayList<Entity>();
		room1Entities.add(angryman);

		Location room1 = new Location(2001, "A Non-descript room",
				"A dainty, run-down room who's atmosphere is almost unbecoming of the hotel it's connected to",
				room1Entities, room1ItemsOnGround);

		// 5. LocationGraph
		LocationGraph maximalGraph = new LocationGraph();

		maximalGraph.addNewLocation(lobby);
		maximalGraph.addNewLocation(room1);

		Path lobbyRoom1Path = new Path(7001, "A sketchy door", "Seems very eerie", lobby, room1, true);
		maximalGraph.addPath(lobbyRoom1Path);

		// 5. Quests

		// 6. Player and Campaign
		List<Integer> knownPathIds = new ArrayList<Integer>();
		//knownPathIds.add(7001);

		Entity playerEntity = new Entity(1000, "kimmu", "A big baby", new Attributes(4, 4, 4, 1), 1, new Inventory(),
				Dialogue.NO_DIALOGUE, false);
		PlayerAvatar player = new PlayerAvatar(playerEntity, lobby, knownPathIds, new QuestLog());

		List<EntityTemplate> entityTemplates = new ArrayList<EntityTemplate>();
		entityTemplates.add(zombie);

		List<Item> campaignItems = new ArrayList<Item>();
		campaignItems.add(reward);

		CampaignData testCampaignData = new CampaignData("A hotel of doom!",
				"A eerie hotel looms before you! Begin your journey and find out its true secrets!", entityTemplates,
				campaignQuests, campaignItems);
		CampaignState testCampaignState = new CampaignState(maximalGraph);
		Campaign campaign = new Campaign(testCampaignData, testCampaignState, player);

		return new Game(campaign, player);
	}

	public Game(Campaign campaign, PlayerAvatar player) {
		this.currentCampaign = campaign;
		this.player = player;

	}

}
