package com.ananotherrpg.inventory;

import com.ananotherrpg.Identifiable;

public class Item implements Identifiable{
	protected int itemId;
	
	protected String name;
	protected int weight;
	protected int sellPrice;
	
	public Item(String name) {
		this.name = name;
	}
	public int getWeight() {
		return weight;
	}
	public int getSellPrice() {
		return sellPrice;
	}
	@Override
	public String getName() {
		return name;
	}
	
	
}
