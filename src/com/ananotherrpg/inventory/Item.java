package com.ananotherrpg.inventory;

import com.ananotherrpg.IIdentifiable;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.io.IOManager;

public class Item implements IIdentifiable{
	private int itemID;

	protected String name;
	protected String description;

	protected int weight;
	protected int sellPrice;

	private boolean consumeOnUse;

	public Item(int itemID, String name, String description, int weight, int sellPrice, boolean consumeOnUse) {
		this.itemID = itemID;
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.sellPrice = sellPrice;

		this.consumeOnUse = consumeOnUse;
	}
	
	public int getWeight() {
		return weight;
	}
	public int getSellPrice() {
		return sellPrice;
	}

	public void use(Entity player){
		IOManager.println("Doesn't really do much does it?");
	}

	public int getID(){
		return itemID;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getListForm() {
		return "Item: " + name;
	}

	@Override
	public String getDetailForm() {
		return name + "\n" + description;
	}

	public boolean shouldConsumeOnUse(){
		return consumeOnUse;
	}
	
}
