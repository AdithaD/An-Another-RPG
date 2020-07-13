package com.ananotherrpg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
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

		Location lobby = new Location("lobby", "An illustrious hotel lobby filled with an air of richness.", lobbyEntities, lobbyItems);

		Item key = new Item("Key");
		ArrayList<ItemStack> room1Items = new ArrayList<ItemStack>();
		room1Items.add(new ItemStack(sword, 1));
		room1Items.add( new ItemStack(key, 1));

		Combatant angryman = new Combatant("Angry Man", 20, 20, new Inventory(), 4, 1, false);
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
		@SuppressWarnings("resource")
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
			if(gameState == State.GAME) {
				System.out.println("What would you like to do?");
				// Player options are: Look around (currentLocation), Enter (Location), Talk
				// (Entity), Help

				ArrayList<String> options = new ArrayList<String>();
				options.add("Look around");
				options.add("Move to");
				options.add("Talk");
				options.add("Help");

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
					moveTo(s);
					break;
				case "Talk":
					talk(s);
					break;
				}
			}
		}
	}
	
	private void talk(Scanner s) {
		// TODO Auto-generated method stub
		
	}

	private void moveTo(Scanner s) {
		System.out.println("Where would you like to go ?");
		
		List<Location> adjacentLocations = campaign
				.getLocations()
				.getAdjacentNodes(campaign.getCurrentLocation());
		
		// Queries the location data from the location nodes and returns it as a Identifiable array
				
		listIdentifiers(adjacentLocations);
		
		String input = s.nextLine();
		Map<String, Location> options = adjacentLocations.stream().collect(Collectors.toMap(Location::getName, Function.identity()));
		
		while(!(options.containsKey(input))) {
			System.out.println("That's not a valid option");
			input = s.nextLine();
		}
		
		campaign.setCurrentLocation(options.get(input));
		System.out.println("You move to " + campaign.getCurrentLocation().getName());
		
	}

	private void lookAround() {
		System.out.println(campaign.getCurrentLocation().getDescription());
		System.out.println("You do a quick whirl and you see:");
				
		listIdentifiers(campaign
				.getCurrentLocation()
				.getPermanentEntities());
		
		listIdentifiers(campaign.getCurrentLocation()
				.getItems()
				.stream()
				.map(ItemStack::getItem)
				.collect(Collectors.toList()));
	}
	
	private void listIdentifiers(List<? extends Identifiable> list) {
		for(Identifiable object : list) {
			System.out.println("* " + object.getName());
		}
	}

	

	private static Campaign chooseSave() {
		return null;

	}

}
