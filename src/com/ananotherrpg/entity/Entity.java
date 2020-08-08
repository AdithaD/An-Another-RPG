package com.ananotherrpg.entity;

import com.ananotherrpg.IIdentifiable;
import com.ananotherrpg.entity.dialogue.Dialogue;
import com.ananotherrpg.entity.dialogue.DialogueTraverser;
import com.ananotherrpg.entity.inventory.Inventory;
import com.ananotherrpg.entity.inventory.Item;
import com.ananotherrpg.entity.inventory.Weapon;

/**
 * The Entity class represents "living beings" in the game
 */
public class Entity implements IIdentifiable {

	private final int entityID;

	private String name;
	private String description;

	private Attributes attributes;
	
	private int level;
	private boolean isDead;

	private Inventory inventory;

	private Dialogue dialogue;

	public Entity(int entityID, String name, String description, Attributes attributes, int level, Inventory inventory,
			Dialogue dialogue, boolean isDead) {
		this.entityID = entityID;
		this.name = name;
		this.description = description;

		this.attributes = attributes;
		this.level = level;

		this.inventory = inventory;

		this.dialogue = dialogue;

		this.isDead = isDead;
	}

	/**
	 * Adds a modifier to the player's attributes
	 * @param attributeModifier The modifier to add
	 */
	public void applyModifiers(AttributeModifier attributeModifier) {
		attributes.addModifier(attributeModifier);
	}

	/**
	 * Uses the specified item.
	 * @param item The item to use
	 */
	public void use(Item item) {
		inventory.use(item, this);
	}

	/**
	 * Sets the weapon as the equipped weapon for the entity
	 * @param weapon The weapon to equip
	 */
	public void equipWeapon(Weapon weapon){
		inventory.equipWeapon(weapon);
	}

	/**
	 * Instatiates a <code>DialogueTraverser</code> between the specific entity and this entity and its dialogue.
	 * @param partner The entity to start the dialogue with
	 * @return A <code>DialogueTraverser</code> to go through the <code>Dialogue</code> with
	 */
	public DialogueTraverser startDialogue(Entity partner) {
		return dialogue.getTraverser(this, partner);
	}

	/**
	 * Calculates the damage this entity would do in an attack.
	 * @return The amount of damage.
	 */
	public int calculateDamage(){
		int damage = 0;
		if(inventory.hasEquippedWeapon()){
			int weaponDamage = inventory.getEquippedWeapon()
			.calculateDamage(attributes.calculateCritChanceBonus(), attributes.calculateCritMultiBonus());
	
			 damage =  (int) Math.round(weaponDamage * attributes.calculateBaseDamageScaling());
			
		}else{
			damage = attributes.calculateUnarmedDamage();
		}

		
		return damage;
	}

	/**
	 * Instructs the entity to die
	 */
	public void die() {
		isDead = true;
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

	

	public int getLevel() {
		return level;
	}

	

	
}
