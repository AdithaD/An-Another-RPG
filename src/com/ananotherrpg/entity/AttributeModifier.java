package com.ananotherrpg.entity;

import com.ananotherrpg.entity.Attributes.Attribute;

public class AttributeModifier {
    public Attribute attribute;
    public int modifier;

    public int duration;

    public AttributeModifier(Attribute attribute, int modifier, int duration) {
        this.attribute = attribute;
        this.modifier = modifier;
        this.duration = duration;
    }

    
}
