package com.ananotherrpg.entity;

import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Weapon;
import com.ananotherrpg.level.Quest;

public class Player extends Combatant {
	
	private int xp;
	
	public Player(String name, int hp, int maxHealth, Inventory inventory, int initiative, int level, Weapon equippedWeapon, Boolean isDead) {
		super(name, hp, maxHealth, inventory, initiative, level, equippedWeapon, false);
		xp = 0;
	}
	
	public Player(String name, int hp, int maxHealth, Inventory inventory, int initiative, int level) {
		super(name, hp, maxHealth, inventory, initiative, level, Weapon.UNARMED, false );
		xp = 0;
		equippedWeapon = new Weapon("Fists of fury",1, 0.5, 4);
	}

	
	

	public void gainXp(int xpgain) {
		xp += xpgain;
	}

	public void addQuest(Quest quest) {

	}

}
