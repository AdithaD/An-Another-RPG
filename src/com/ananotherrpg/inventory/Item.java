package com.ananotherrpg.inventory;

import com.ananotherrpg.entity.Player;

public class Item {
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
	public String getName() {
		return name;
	}
	
	public void use(Player player){
		player.accept(this);
	}
}
