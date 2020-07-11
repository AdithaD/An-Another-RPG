package com.ananotherrpg.entity;

import com.ananotherrpg.inventory.Inventory;

public class Entity {
	private int entityId;
	
	private int hp;
	private int maxHealth;
	private Inventory inventory;
	
	private Boolean isDead;
	
	private void takeDamage(int damage) {
		hp -= damage;
	}
}
