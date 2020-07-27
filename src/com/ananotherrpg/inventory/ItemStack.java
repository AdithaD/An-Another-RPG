package com.ananotherrpg.inventory;

import com.ananotherrpg.IQueryable;

public class ItemStack implements IQueryable{
    private Item item;
    private int quantity;

    public ItemStack(Item item, Integer quantity) {
        this.item = item;
        this.quantity = quantity;
    }


	public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String getName() {
        return item.name;
    }

    @Override
    public String getListForm() {
        return item.name + " x" + quantity;
    }

}