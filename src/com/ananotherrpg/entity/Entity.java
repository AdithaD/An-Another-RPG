package com.ananotherrpg.entity;

import com.ananotherrpg.IIdentifiable;
import com.ananotherrpg.entity.dialogue.Dialogue;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Weapon;

/**
 * The Entity class represents "living beings" in the game
 */
public class Entity implements IIdentifiable {

	private final int entityID;

	private String name;
	private String description;

	private int hp;
	private Attributes attributes;
	
	private int level;
	private int xp;
	private boolean isDead;

	private Inventory inventory;
	

	private Dialogue dialogue;

	public Entity(int entityID, String name, String description, Attributes attributes, int level, 
			Inventory inventory, Dialogue dialogue, boolean isDead) {
		this.entityID = entityID;
		this.name = name;
		this.description = description;

		this.attributes = attributes;
		this.level = level;
		this.hp = attributes.calculateMaxHp();
		this.xp = 0;

		this.inventory = inventory;

		this.dialogue = dialogue;

		this.isDead = isDead;
	}

	public Entity(int entityID, String name, String description, Attributes attributes, int hp, int level, int xp,
			Inventory inventory, Dialogue dialogue, boolean isDead) {
		this.entityID = entityID;
		this.name = name;
		this.description = description;

		this.attributes = attributes;
		this.hp = hp;
		this.level = level;
		this.xp = xp;

		this.inventory = inventory;

		this.dialogue = dialogue;

		this.isDead = isDead;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public String getDescription() {
		
		return description;
	}

	public boolean isDead() {
		return isDead;
	}
	
	public Dialogue getDialogue(){
		return dialogue;
	}

	public void equipWeapon(Weapon weapon){
		//update item effects
		inventory.equipWeapon(weapon);
	}

	public void applyModifiers(AttributeModifier attributeModifier) {
		attributes.addModifier(attributeModifier);
	}

	@Override
	public String getListForm() {
		return name;
	}

	@Override
	public String getDetailForm() {
		String detailForm  = name + ": \n" + description;

		return detailForm;
	}

	public Attributes getAttributes(){
		return attributes;
	}

	public Inventory getInventory(){
		return inventory;
	}

	@Override
	public int getID() {
		return entityID;
	}

	
}
