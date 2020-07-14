package com.ananotherrpg.entity;

import com.ananotherrpg.Identifiable;
import com.ananotherrpg.inventory.Inventory;

public class Entity implements Identifiable{
	private int entityId;
	
	protected String name;
	protected Boolean isKnown = true;
	
	public String getName() {
		return name;
	}



	protected int hp;
	protected int maxHealth;
	protected Inventory inventory;
	
	protected Boolean isDead;

	public Entity(int entityId) {
		this.entityId = entityId;
	}

	

	public Entity(String name, int hp, int maxHealth, Inventory inventory, Boolean isDead) {
		this.name = name;
		this.hp = hp;
		this.maxHealth = maxHealth;
		this.inventory = inventory;
		this.isDead = isDead;
	}
	
	public Entity(String name, int hp, int maxHealth, Inventory inventory, Boolean isDead, Boolean isKnown) {
		this.name = name;
		this.hp = hp;
		this.maxHealth = maxHealth;
		this.inventory = inventory;
		this.isDead = isDead;
		this.isKnown = isKnown;
	}


	private void takeDamage(int damage) {
		hp -= damage;
	}
	
	public Boolean IsDead() {
		return isDead;
	}



	public void setIsDead(Boolean isDead) {
		this.isDead = isDead;
	}
	
	public Boolean isKnown() {
		return isKnown;
	}
}
