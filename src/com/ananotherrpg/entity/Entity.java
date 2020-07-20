package com.ananotherrpg.entity;

import com.ananotherrpg.Identifiable;
import com.ananotherrpg.entity.dialogue.DialogueLine;
import com.ananotherrpg.entity.dialogue.DialogueManager;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.util.DirectedDataGraph;

public class Entity implements Identifiable {
	private int entityId;

	protected String name;

	protected DirectedDataGraph<DialogueLine, String> dialogue;

	protected int hp;
	protected int maxHealth;
	public Inventory inventory;

	protected Boolean isDead;

	public Entity(int entityId) {
		this.entityId = entityId;
	}

	public Entity(String name, int hp, int maxHealth, Inventory inventory) {
		this.name = name;
		this.hp = hp;
		this.maxHealth = maxHealth;
		this.inventory = inventory;
		this.isDead = false;
		this.dialogue = DialogueManager.NO_DIALOGUE;
	}

	public Entity(String name, int hp, int maxHealth, Inventory inventory, Boolean isDead, Boolean isKnown,
	DirectedDataGraph<DialogueLine, String> dialogue) {
		this.name = name;
		this.hp = hp;
		this.maxHealth = maxHealth;
		this.inventory = inventory;
		this.isDead = isDead;;
		this.dialogue = dialogue;
	}

	public Boolean IsDead() {
		return isDead;
	}

	public void setIsDead(Boolean isDead) {
		this.isDead = isDead;
	}

	public String getName() {
		return name;
	}

	public void addItemStackToInventory(ItemStack itemStack){
		inventory.addToInventory(itemStack);
	}

	public Inventory getInventory(){
		return inventory;
	}

	public DirectedDataGraph<DialogueLine, String> getDialogueGraph(){
		return dialogue;
	}

	

	@Override
	public String getDetails() {
		return name;
	}
}
