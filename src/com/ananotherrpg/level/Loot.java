package com.ananotherrpg.level;

public class Loot {
    private int itemID;

    private int minimum;
    private int maximum;

    public Loot(int itemID, int minimum, int maximum) {
        this.itemID = itemID;
        this.minimum = minimum;
        this.maximum = maximum;
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
