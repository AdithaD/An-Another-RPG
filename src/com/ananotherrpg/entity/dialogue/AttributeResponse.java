package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.entity.Attributes.Attribute;

public class AttributeResponse extends Response {

    private Attribute attribute;
    private int minimumAmount;

    public AttributeResponse(DialogueLine incidentLine, String responseText, Attribute attribute, int minimumAmount) {
        super(incidentLine, responseText);
        
        this.attribute = attribute;
        this.minimumAmount = minimumAmount;
    }
}