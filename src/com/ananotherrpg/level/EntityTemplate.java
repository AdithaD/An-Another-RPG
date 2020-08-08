package com.ananotherrpg.level;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import com.ananotherrpg.entity.Attributes;
import com.ananotherrpg.entity.Attributes.Attribute;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.dialogue.Dialogue;
import com.ananotherrpg.entity.dialogue.DialogueGraph;
import com.ananotherrpg.entity.inventory.Inventory;
import com.ananotherrpg.entity.inventory.Item;
import com.ananotherrpg.entity.inventory.Weapon;
import com.ananotherrpg.level.quest.Loot;

/**
 * A stateless form of an entity, which needs to be instantiated to a Entity before usage in the world.
 */
public class EntityTemplate{
    private final int entityID;

    private String name;
    private String description;

    private int equippedWeaponID;
    private List<Loot> possibleLoot;

    private int dialogueID;

    //each attribute has a value of 1 to 10 to define its weighting of attribute points
    private EnumMap<Attribute, Integer> attributeDistribution; 

    /**
     * Generates a set of <code>Attributes</code> from the <code>attributeDistribution</code> at a specific level.
     * <p> Higher level means more attribute points to distribute.
     * @param level The level to instantiate the creature at. 
     * @return A set of <code>Attributes</code> suitable for the level specified.
     */
    private Attributes generateScaledAttributes(int level){
        
        int noOfAttributePoints  = (Attribute.values().length * 2) + 2 * level;

        EnumMap<Attribute, Integer> attributeValues = new EnumMap<Attribute, Integer>(Attribute.class);

        for (Attribute attribute : Attribute.values()) {
            int amount = (int) Math.round(6 + (noOfAttributePoints * (attributeDistribution.get(attribute)/10.0)));
            attributeValues.put(attribute, amount);
        }
        return new Attributes(attributeValues);
    }

    /**
     * Generates an inventory from the specified loot
     * @param items A map of itemIDs to Item objects
     * @return A inventory containing itemStacks generated from Loot.
     */
    private Inventory generateInventory(Map<Integer, Item> items) {
        Inventory inv = new Inventory();

        Weapon equippedWeapon = (Weapon) items.get(equippedWeaponID);

        for (Loot loot : possibleLoot) {
            inv.addToInventory(loot.generateLootStack(items));
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
