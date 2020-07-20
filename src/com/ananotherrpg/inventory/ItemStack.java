package com.ananotherrpg.inventory;

import com.ananotherrpg.Identifiable;

public class ItemStack implements Identifiable {

	private Item item;
	private int quantity;


	
	public ItemStack(Item item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}
	
	public void increaseQuantityBy(int quantity){
		this.quantity += quantity;
	}

	public void reduceQuantityBy(int quantity){
		this.quantity -= quantity;
		if(quantity < 0 ) { quantity = 0; }
	}

	public Boolean isEmpty(){
		return quantity <= 0;
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
	public String getDetails() {
		return item.getName() + " x" + quantity;
	}
}
