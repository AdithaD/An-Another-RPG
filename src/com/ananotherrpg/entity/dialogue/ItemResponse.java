package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.entity.Player;
import com.ananotherrpg.inventory.Item;

public class ItemResponse extends Response {
    private int itemID;

    @Override
    public void reevaluateActiveStatus(Player player) {
        isActive = player.getEntity().getInventory().hasID(itemID);
    }
}