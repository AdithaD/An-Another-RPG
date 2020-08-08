package com.ananotherrpg.entity;

import com.ananotherrpg.entity.Attributes.Attribute;

/**
 * A temporary modifier applied on an entity's attributes.
 */
public class AttributeModifier {

    private Attribute attribute;
    private int modifier;

    // Duration is measured in amount of turns the effect will be active.
    private int duration;
    
    // Turns passed = ticks
    private int ticksPassed;

    public AttributeModifier(Attribute attribute, int modifier, int duration) {
        this.attribute = attribute;
        this.modifier = modifier;
        this.duration = duration;

        this.ticksPassed = 0;
    }

    public boolean isModifierOver(){
        return ticksPassed >= duration;
    }

    public void tick(){
        ticksPassed += 1;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public int getModifier() {
        return modifier;
    }
}
