package com.ananotherrpg.level.quest;
/**
 * Loots that will be dropped by a entity on kill. This is for generic items such as money or materials, as it 
 */

import java.util.Map;
import java.util.Random;

import com.ananotherrpg.entity.inventory.Item;
import com.ananotherrpg.entity.inventory.ItemStack;

public class Loot {
    private int itemID;

    private int minimum;
    private int maximum;

    public Loot(int itemID, int minimum, int maximum) {
        this.itemID = itemID;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Generates an <code>ItemStack</code> of the itemID item with random quantity between the maximum and minimum.
     * @param items A itemID to Item map.
     * @return An random <code>ItemStack</code> of item itemID.
     */
	public ItemStack generateLootStack(Map<Integer, Item> items) {
        Random r =  new Random();
        
        return new ItemStack(items.get(itemID), r.nextInt(maximum- minimum)+ minimum);
	}

    public int getItemID() {
        return itemID;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getMinimum() {
        return minimum;
    }


    
}
