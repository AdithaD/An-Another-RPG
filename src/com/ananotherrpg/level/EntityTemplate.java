package com.ananotherrpg.level;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ananotherrpg.entity.Attributes;
import com.ananotherrpg.entity.Attributes.Attribute;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.dialogue.Dialogue;
import com.ananotherrpg.entity.dialogue.DialogueGraph;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.Weapon;

public class EntityTemplate{
    private final int entityID;

    private String name;
    private String description;

    private int equippedWeaponID;
    private List<Loot> possibleLoot;

    private int dialogueID;

    private EnumMap<Attribute, Integer> attributeDistribution; //each attribute has a value of 1 to 10

    private Attributes generateScaledAttributes(int level){
        
        int noOfAttributePoints  = (Attribute.values().length * 2) + 2 * level;

        EnumMap<Attribute, Integer> attributeValues = new EnumMap<Attribute, Integer>(Attribute.class);

        for (Attribute attribute : Attribute.values()) {
            int amount = (int) Math.round(6 + (noOfAttributePoints * (attributeDistribution.get(attribute)/10.0)));
            attributeValues.put(attribute, amount);
        }
        return new Attributes(attributeValues);
    }

    private Inventory generateInventory(Map<Integer, Item> items) {
        Inventory inv = new Inventory();

        Weapon equippedWeapon = (Weapon) items.get(equippedWeaponID);

        Random r = new Random();
        for (Loot loot : possibleLoot) {
            inv.addToInventory(items.get(loot.getItemID()), r.nextInt(loot.getMaximum()-loot.getMinimum())+loot.getMinimum());
        }

        if(equippedWeapon != null) inv.equipWeapon(equippedWeapon);
        return inv;
    }

    public EntityTemplate(int entityID, String name, String description, int equippedWeaponID, List<Loot> possibleLoot, int dialogueID,
            EnumMap<Attribute, Integer> attributeDistribution) {
        this.entityID = entityID;

        this.name = name;
        this.description = description;
        
        
        this.equippedWeaponID = equippedWeaponID;
        this.possibleLoot = possibleLoot;

        this.dialogueID = dialogueID;

        this.attributeDistribution = attributeDistribution;
    }
    public int getID(){
        return entityID;
    }

	public Entity instantiateTemplate(int level, Map<Integer, Item> items, Map<Integer, DialogueGraph> dialogueTemplates) {
		return new Entity(entityID, name, description, generateScaledAttributes(level), level, generateInventory(items), new Dialogue(dialogueTemplates.get(dialogueID)), false);
	}
}
