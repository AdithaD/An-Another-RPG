package com.ananotherrpg;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Combatant;
import com.ananotherrpg.entity.Entity;
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
import com.ananotherrpg.util.Node;

public class Game {

	private Campaign campaign;

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

		Entity manager = new Entity("Manager", 20, 20, new Inventory(), false);
		ArrayList<Entity> lobbyEntities = new ArrayList<Entity>();
		lobbyEntities.add(manager);

		Location lobby = new Location("lobby", lobbyEntities, lobbyItems);

		Item key = new Item("Key");
		ArrayList<ItemStack> room1Items = new ArrayList<ItemStack>();
		room1Items.add(new ItemStack(sword, 1));
		room1Items.add( new ItemStack(key, 1));

		Combatant angryman = new Combatant("Angry Man", 20, 20, new Inventory(), 4, 1, false);
		ArrayList<Entity> room1Entities = new ArrayList<Entity>();
		room1Entities.add(angryman);

		Location room1 = new Location("A Non-descript room", room1Entities, room1Items);

		Graph<Location> locations = new Graph<Location>();

		Node<Location> lobbyNode = new Node<Location>(lobby.getName(), lobby);
		Node<Location> room1Node = new Node<Location>(room1.getName(), room1);

		locations.addNode(lobbyNode);
		locations.addNode(room1Node);

		locations.addEdge(lobbyNode, room1Node);

		ArrayList<Combatant> killTargets = new ArrayList<Combatant>();
		killTargets.add(angryman);

		Objective killAngryMan = new KillObjective(1, "Kill the angry man", room1, killTargets);
		Objective[] objectives = { killAngryMan };

		Quest killAngryBby = new Quest(22, objectives, new Quest[0], false, false);

		Campaign testCampaign = new Campaign(campaignID,
				"A beautiful hotel lobby with an inconspicuous room to the side", locations, lobbyNode,
				killAngryBby);

		campaign = testCampaign;

	}

	public Game(Campaign campaign) {

	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.println("Welcome to an another rpg!");
		System.out.println("Would you like to: \n 1 | Start a new campaign 2 | Load a previous campaign 3 | Quit ");

		int input;
		do {
			while (!s.hasNextInt()) {
				System.out.println("That's not a valid option");
				s.next();
			}
			input = s.nextInt();
		} while (!(input == 1 || input == 2 || input == 3));

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

		Scanner s = new Scanner(System.in);

		System.out.println(campaign.getIntroduction());
		while (shouldKeepPlaying) {
			System.out.println("what would you like to do?");
			// Player options are: Look around (currentLocation), Enter (Location), Talk
			// (Entity), Help

			ArrayList<String> options = new ArrayList<String>();
			options.add("Look around");
			options.add("Enter");
			options.add("Talk");
			options.add("Help");

			String input = s.next();
			while (!options.contains(input)) {
				System.out.println("That's not a valid option");
				input = s.nextLine();
			}

			switch (input) {
			case "Look around":
				lookAround();
				break;
			case "Enter":
				enter(s);
				break;
			case "Talk":
				talk(s);
				break;
			}
		}
	
	}
	
	private void talk(Scanner s) {
		// TODO Auto-generated method stub
		
	}

	private void enter(Scanner s) {
		System.out.println("Where would you like to go ?");
		
		List<Node<Location>> adjacentLocations = campaign
				.getLocations()
				.getAdjacentNodes(campaign.getCurrentLocation());
		
		// Queries the location data from the location nodes and returns it as a Identifiable array
		List<Location> locationsData = adjacentLocations
				.stream()
				.map(Node<Location>::getData)
				.collect(Collectors.toList());
				
		listNames(locationsData);
		
		String input = s.nextLine();
		List<String> options = locationsData.stream().map(Location::getName).collect(Collectors.toList());
		
		while(!(options.contains(input))) {
			System.out.println("That's not a valid option");
			input = s.nextLine();
		}
		
		
	}

	private void lookAround() {
		System.out.println("You do a quick whirl and you see:");
				
		listNames(campaign
				.getCurrentLocation()
				.getData()
				.getPermanentEntities());
		
		listNames(campaign.getCurrentLocation()
				.getData()
				.getItems()
				.stream()
				.map(ItemStack::getItem)
				.collect(Collectors.toList()));
	}
	
	private void listNames(List<? extends Identifiable> list) {
		for(Identifiable object : list) {
			System.out.println("* " + object.getName());
		}
	}

	

	private static Campaign chooseSave() {
		return null;

	}

}
