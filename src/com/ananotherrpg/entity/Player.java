package com.ananotherrpg.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ananotherrpg.Identifiable;
import com.ananotherrpg.entity.dialogue.DialogueManager;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.inventory.Weapon;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.LocationManager;
import com.ananotherrpg.level.Objective;
import com.ananotherrpg.level.Quest;

public class Player extends Combatant {

	private final ArrayList<String> options = new ArrayList<String>(
			Arrays.asList(new String[] { "Look around", "Move to", "Examine", "Talk", "View Quests", "View Inventory", "Pick Up", "Help" }));
	
	private int xp;
	private LocationManager locationManager;
	private DialogueManager dialogueManager;

	private List<Quest> activeQuests;

	private boolean shouldExitCampaign = false;

	public Player(String name, int hp, int maxHealth, Inventory inventory, int initiative, int level, Weapon equippedWeapon, Boolean isDead, LocationManager locationManager, DialogueManager dialogueManager) {
		super(name, hp, maxHealth, inventory, initiative, level, equippedWeapon, false);

		this.locationManager = locationManager;
		this.dialogueManager = dialogueManager;

		xp = 0;
	}
	
	public Player(String name, int hp, int maxHealth, Inventory inventory, int initiative, int level, LocationManager locationManager, DialogueManager dialogueManager) {
		super(name, hp, maxHealth, inventory, initiative, level, Weapon.UNARMED, false );
		xp = 0;
		equippedWeapon = Weapon.UNARMED;
	
		this.locationManager = locationManager;
		this.dialogueManager = dialogueManager;
	}

	public void loop(){
		IOManager.println("What would you like to do? (Type help for options)");
		Optional<String> opInput = IOManager.queryUserInputAgainstStrings(options, SelectionMethod.TEXT);

		if (opInput.isPresent()) {
			switch (opInput.get()) {
				case "Look around":
					locationManager.lookAround();
					break;
				case "Move to":
					locationManager.moveTo();
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

	public void talk() {
		if (locationManager.getPermanentEntitiesInCurrentLocation().isEmpty()) {
			IOManager.println("There is no one to talk to");
		} else {

			IOManager.println("Who would you like to talk to?");
			Optional<Entity> opEntity = IOManager.listAndQueryUserInputAgainstIdentifiers(
					locationManager.getPermanentEntitiesInCurrentLocation(), ListType.NUMBERED, SelectionMethod.NUMBERED);

			if (opEntity.isPresent()) {
				dialogueManager.initiateDialogue(opEntity.get());

				activeQuests.addAll(dialogueManager.getNewQuests());
			} else {
				IOManager.println("You decide otherwise");
			}

		}
	}

	public void moveTo() {
		locationManager.moveTo();
	}

	public void lookAround() {
		locationManager.lookAround();
	}

	public void examine(){
		List<? extends Identifiable> identifiables = locationManager.getListOfIdentifiablesInCurrentLocation(); 

		IOManager.println("What would you like to examine: (Type its name)");
		Optional<? extends Identifiable> opIdentifiable = IOManager.queryUserInputAgainstIdentifiers(identifiables, SelectionMethod.TEXT);

		if(opIdentifiable.isPresent()){
			IOManager.println(opIdentifiable.get().getDetails());
		}else{
			IOManager.println("You know enough already. Evidently...");
		}
	}

	public void viewQuests() {
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
		Optional<ItemStack> opItem = IOManager.listAndQueryUserInputAgainstIdentifiers(locationManager.getCurrentLocation().getItemStacks(), ListType.NUMBERED, SelectionMethod.NUMBERED);

		if(opItem.isPresent()){
			inventory.addToInventory(opItem.get());
		}else{
			IOManager.println("Your rucksack is doesn't need another item, you say.");
		}
	}

	public void viewInventory() {
		IOManager.println("Your inventory contains: ");
		IOManager.listIdentifiers(inventory.getItemStacks(), ListType.NUMBERED);

		IOManager.println("Enter a number to interact with that Item");
		Optional<ItemStack> opItemStack = IOManager.queryUserInputAgainstIdentifiers(inventory.getItemStacks(), SelectionMethod.NUMBERED);

		if(opItemStack.isPresent()){
			opItemStack.get().getItem().use(this);
		}else{

		}
			
	}

	public void accept(Item item) {
		IOManager.println(item.getName() + " doesn't have much use right now!");
    }
    
    public void equip(Weapon weapon){
		if(IOManager.askYesOrNoQuestion("Would you like to equip this weapon?")){
			IOManager.println("You have equipped " + weapon.getName());
		}else{
			equippedWeapon = weapon;
		}
	}
	
	public boolean shouldExitCampaign(){
		return shouldExitCampaign;
	}

	public void gainXp(int xpgain) {
		xp += xpgain;
	}

}
