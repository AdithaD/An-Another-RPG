package com.ananotherrpg.level;

import java.util.EnumMap;
import java.util.List;

import com.ananotherrpg.entity.Attributes.Attribute;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.inventory.Item;

public class EntityTemplate {

    private String name;
    private String description;

    private List<Item> possibleLoot;

    private EnumMap<Attribute, Integer> attributeDistribution;

    public Entity generateEnemy(){
        //TODO Implement Enemy generation from Template
        return null;
    }
}
