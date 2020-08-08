package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.Attributes.Attribute;

/**
 * A response that is only viable if the player has a <code>minimumAmount</code> of a specified <code>Attribute</code>
 */
public class AttributeResponse extends Response {

    private Attribute attribute;
    private int minimumAmount;

    public AttributeResponse(DialogueLine incidentLine, String responseText, Attribute attribute, int minimumAmount) {
        super(incidentLine, responseText);
        
        this.attribute = attribute;
        this.minimumAmount = minimumAmount;
    }

    @Override
    public boolean isViable(Entity source, Entity player) {
        return player.getAttributes().getAttributePoints(attribute) > minimumAmount;
    }
}