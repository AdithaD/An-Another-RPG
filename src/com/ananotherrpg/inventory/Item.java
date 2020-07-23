package com.ananotherrpg.inventory;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.io.IOManager;

public class Item {
	private int itemID;

	protected String name;
	protected String description;

	protected int weight;
	protected int sellPrice;
	
	public int getWeight() {
		return weight;
	}
	public int getSellPrice() {
		return sellPrice;
	}
	public String getName() {
		return name;
	}

	public void use(Entity player){
		IOManager.println("Doesn't really do much does it?");
	}

	public int getID(){
		return itemID;
	}
}
