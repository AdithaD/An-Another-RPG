package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.entity.Player;

public class ItemResponse extends Response {

    private int itemID;

    public ItemResponse(DialogueLine incidentLine, String responseText, int itemID) {
        super(incidentLine, responseText);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void reevaluateActiveStatus(Player player) {
        isActive = player.getEntity().getInventory().hasID(itemID);
    }
}