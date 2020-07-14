package com.ananotherrpg.inventory;

import com.ananotherrpg.Identifiable;

public class ItemStack implements Identifiable {

	private Item item;
	private int quantity;
	
	private Boolean isKnown = true;
	
	public ItemStack(Item item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	public ItemStack(Item item, int quantity, Boolean isKnown) {
		this.item = item;
		this.quantity = quantity;
		this.isKnown = isKnown;
	}
	
	public int getWeight() {
		return item.getWeight() * quantity;
	}
	
	public Item getItem() {
		return item;
	}

	@Override
	public String getName() {
		return item.getName();
	}

	@Override
	public Boolean isKnown() {
		return isKnown;
	}
}
