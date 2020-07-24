package com.ananotherrpg.level;

import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;

import com.ananotherrpg.entity.Attributes;
import com.ananotherrpg.entity.Attributes.Attribute;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.dialogue.Dialogue;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.Weapon;

public class EntityTemplate{
    private final int entityID;

    private String name;
    private String description;

    private Weapon equippedWeapon;
    private List<Item> possibleLoot;

    private Dialogue dialogue;

    private EnumMap<Attribute, Integer> attributeDistribution;

    private Attributes generateScaledAttributes(int level){
        return null;
    }

    private Inventory generateInventory(){
        // TODO inventory generation
        Inventory inv = new Inventory();
        if(equippedWeapon != null) inv.equipWeapon(equippedWeapon);
        return inv;
    }

    public EntityTemplate(int entityID, String name, String description, Weapon equippedWeapon, List<Item> possibleLoot, Dialogue dialogue,
            EnumMap<Attribute, Integer> attributeDistribution) {
        this.entityID = entityID;

        this.name = name;
        this.description = description;
        
        this.equippedWeapon = equippedWeapon;
        this.possibleLoot = possibleLoot;

        this.dialogue = dialogue;

        this.attributeDistribution = attributeDistribution;
    }

    public Entity instantiateTemplate(int ID, int level) {
        return new Entity(ID, name, description, generateScaledAttributes(level), level, generateInventory(), dialogue, false);
    }

    public int getID(){
        return entityID;
    }
}
