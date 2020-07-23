package com.ananotherrpg.entity;

import com.ananotherrpg.IIdentifiable;
import com.ananotherrpg.entity.dialogue.Dialogue;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Weapon;

/**
 * The Entity class represents "living beings" in the game
 */
public class Entity implements IIdentifiable {

	private String name;
	private String description;

	private int hp;
	private Attributes attributes;
	private int level;
	private boolean isDead;

	private Inventory inventory;
	private Weapon equippedWeapon;

	private Dialogue dialogue;

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
		equippedWeapon = weapon;
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
}
