package com.ananotherrpg.entity;

import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Weapon;

public class Combatant extends Entity {
	
	protected int initiative;
	protected int level;
	protected Weapon equippedWeapon;
	
	public Combatant(int entityId) {
		super(entityId);
		// TODO Auto-generated constructor stub
	}

	public Combatant(String name, int hp, int maxHealth, Inventory inventory, int initiative, int level, Weapon equippedWeapon, Boolean isDead) {
		super(name, hp, maxHealth, inventory, isDead);
		this.initiative = initiative;
		this.level = level;
		this.equippedWeapon = equippedWeapon;
	}
	
	public Combatant(String name, int hp, int maxHealth, Inventory inventory, int initiative, int level, Boolean isDead) {
		super(name, hp, maxHealth, inventory, isDead);
		this.initiative = initiative;
		this.level = level;
	}
	
}
