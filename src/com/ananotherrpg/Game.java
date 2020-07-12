package com.ananotherrpg;

import java.util.Scanner;

import com.ananotherrpg.entity.Combatant;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.inventory.Weapon;
import com.ananotherrpg.level.Campaign;
import com.ananotherrpg.level.Location;
import com.ananotherrpg.level.Objective;
import com.ananotherrpg.level.Quest;
import com.ananotherrpg.util.Graph;
import com.ananotherrpg.util.Node;

public class Game {

	private Campaign campaign;
	
	
	private enum State{
		MENU, GAME, COMBAT
	}
	
	private State gameState = State.MENU;
	
	private Boolean shouldKeepPlaying = false;
	
	public Game() {
		int campaignID = 1;
		
		Weapon sword = new Weapon("Mjollnir", 3, 0.2, 2);
		ItemStack[] lobbyItems = {new ItemStack(sword,1)};
		
		
		Entity manager = new Entity("Manager", 20, 20, new Inventory(), false);
		Entity[] lobbyEntities = {manager};
		
		Location lobby = new Location("lobby", lobbyEntities, lobbyItems);
		
		Item key = new Item("Key");
		ItemStack[] room1Items = {new ItemStack(sword, 1)};
		
		
		Combatant angryman = new Combatant("Angry Man" , 20, 20, new Inventory(), 4, 1, false);
		Entity[] room1Entities = {angryman};
		
		Location room1 = new Location("Room 1", room1Entities, room1Items);
				
		Graph<Location> locations = new Graph<Location>();
		
		Node<Location> lobbyNode = new Node<Location>(lobby.getName(), lobby);
		Node<Location> room1Node = new Node<Location>(room1.getName(), room1);
		
		locations.addNode(lobbyNode);
		locations.addNode(room1Node);
		
		locations.addEdge(lobbyNode, room1Node);
		
		Objective killAngryMan = new Objective(1, "Kill the angry man", room1);
		Objective[] objectives = {killAngryMan};
		
		Quest killAngryBby = new Quest(22, objectives, new Quest[0], false, false);
		
		Campaign testCampaign = new Campaign(campaignID, locations, lobbyNode.getData(), killAngryBby);
		
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
			while(!s.hasNextInt()) {
				System.out.println("That's not a valid option");
				s.next();
			}
			input = s.nextInt();
		} while (!(input == 1 || input == 2 || input == 3));
		
		Game game;
		
		if(input == 3) {
			System.exit(0);
		}else {
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
		
		while (!shouldKeepPlaying) {
			
		}
		
	}

	private static Campaign chooseSave() {
		return null;
		
	}
	

}
