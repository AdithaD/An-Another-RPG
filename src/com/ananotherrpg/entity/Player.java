package com.ananotherrpg.entity;

import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Weapon;

public class Player extends Combatant {
	
	private int xp;
	
	
	public Player(String name, int hp, int maxHealth, Inventory inventory, int initiative, int level, Weapon equippedWeapon) {
		super(name, hp, maxHealth, inventory, initiative, level, equippedWeapon, false);
		// TODO Auto-generated constructor stub
	}
	
	public Player(String name, int hp, int maxHealth, Inventory inventory, int initiative, int level) {
		super(name, hp, maxHealth, inventory, initiative, level, false);
		// TODO Auto-generated constructor stub
	}

	
	

	public void gainXp(int xpgain) {
		xp += xpgain;
	}
	
	private void levelUp() {
		
	}
}
