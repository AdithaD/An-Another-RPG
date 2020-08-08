package com.ananotherrpg.entity.inventory;


import com.ananotherrpg.entity.AttributeModifier;
import com.ananotherrpg.entity.Entity;

public class Potion extends Item{
    public Potion(int itemID, String name, String description, String interactText, int weight, int sellPrice, AttributeModifier attributeModifier){
        super(itemID, name, description, interactText, weight, sellPrice, true);
        this.attributeModifier = attributeModifier;
    }

    private AttributeModifier attributeModifier;

    @Override
    public void use(Entity player) {
        player.applyModifiers(attributeModifier);
    }
}