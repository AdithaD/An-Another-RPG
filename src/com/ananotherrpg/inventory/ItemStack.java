package com.ananotherrpg.inventory;

public class ItemStack {

	private Item item;
	private int quantity;
	
	
	public ItemStack(Item item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}
	
	public int getWeight() {
		return item.getWeight() * quantity;
	}
	
	public Item getItem() {
		return item;
	}
}
