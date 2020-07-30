package com.ananotherrpg.entity;

import com.ananotherrpg.IIdentifiable;
import com.ananotherrpg.entity.dialogue.Dialogue;
import com.ananotherrpg.entity.dialogue.DialogueTraverser;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.Weapon;
import com.ananotherrpg.io.IOManager;

/**
 * The Entity class represents "living beings" in the game
 */
public class Entity implements IIdentifiable {

	private final int entityID;

	private String name;
	private String description;

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
		this.xp = 0;

		this.inventory = inventory;

		this.dialogue = dialogue;

		this.isDead = isDead;
	}

	public Entity(int entityID, String name, String description, Attributes attributes, int level, int xp,
			Inventory inventory, Dialogue dialogue, boolean isDead) {
		this.entityID = entityID;
		this.name = name;
		this.description = description;

		this.attributes = attributes;
		this.level = level;
		this.xp = xp;

		this.inventory = inventory;

		this.dialogue = dialogue;

		this.isDead = isDead;
	}

	public void equipWeapon(Weapon weapon){
		IOManager.println("You equip the weapon " + weapon.getName());
		inventory.equipWeapon(weapon);
	}

	public void applyModifiers(AttributeModifier attributeModifier) {
		attributes.addModifier(attributeModifier);
	}

	public void use(Item item) {
		inventory.use(item, this);
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

	public DialogueTraverser startDialogue(Entity playerEntity) {
		return dialogue.getTraverser(this, playerEntity);
	}

	public int getLevel() {
		return level;
	}
}
