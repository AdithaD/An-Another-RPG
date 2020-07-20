package com.ananotherrpg.entity;

import java.util.List;
import java.util.Optional;

import com.ananotherrpg.entity.dialogue.DialogueManager;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.inventory.Weapon;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;

public class Player extends Combatant {
	
	private int xp;
	private DialogueManager dialogueManager;

	
	
	public Player(String name, int hp, int maxHealth, Inventory inventory, int initiative, int level, Weapon equippedWeapon, Boolean isDead) {
		super(name, hp, maxHealth, inventory, initiative, level, equippedWeapon, false);

		xp = 0;
	}
	
	public Player(String name, int hp, int maxHealth, Inventory inventory, int initiative, int level) {
		super(name, hp, maxHealth, inventory, initiative, level, Weapon.UNARMED, false );
		xp = 0;
		equippedWeapon = new Weapon("Fists of fury",1, 0.5, 4);
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
	

	public void gainXp(int xpgain) {
		xp += xpgain;
	}

}
