package com.ananotherrpg.inventory;

import com.ananotherrpg.entity.AttributeModifier;
import com.ananotherrpg.entity.Entity;

public class Potion extends Item{
    public Potion(int itemID, String name, String description, int weight, int sellPrice) {
        super(itemID, name, description, weight, sellPrice, true);
        // TODO Auto-generated constructor stub
    }

    private AttributeModifier attributeModifier;

    @Override
    public void use(Entity player) {
        player.applyModifiers(attributeModifier);
    }
}