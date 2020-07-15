package com.ananotherrpg.entity;

import com.ananotherrpg.Identifiable;
import com.ananotherrpg.inventory.Inventory;

public class Entity implements Identifiable {
	private int entityId;

	protected String name;

	protected DialogueGraph dialogue;

	protected int hp;
	protected int maxHealth;
	protected Inventory inventory;

	protected Boolean isDead;
	protected Boolean isKnown;

	public Entity(int entityId) {
		this.entityId = entityId;
	}

	public Entity(String name, int hp, int maxHealth, Inventory inventory) {
		this.name = name;
		this.hp = hp;
		this.maxHealth = maxHealth;
		this.inventory = inventory;
		this.isDead = false;
		this.isKnown = true;
		this.dialogue = DialogueGraph.NO_DIALOGUE;
	}

	public Entity(String name, int hp, int maxHealth, Inventory inventory, Boolean isDead, Boolean isKnown,
	DialogueGraph dialogue) {
		this.name = name;
		this.hp = hp;
		this.maxHealth = maxHealth;
		this.inventory = inventory;
		this.isDead = isDead;
		this.isKnown = isKnown;
		this.dialogue = dialogue;
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

	public String getName() {
		return name;
	}

	public DialogueGraph getDialogueGraph(){
		return dialogue;
	}

}
