package com.ananotherrpg.inventory;

import com.ananotherrpg.entity.AttributeModifier;
import com.ananotherrpg.entity.Entity;

public class Potion extends Item{
    private AttributeModifier attributeModifier;

    @Override
    public void use(Entity player) {
        player.applyModifiers(attributeModifier);
    }
}