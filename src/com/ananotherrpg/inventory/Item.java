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

	public Item(int itemID, String name, String description, int weight, int sellPrice) {
		this.itemID = itemID;
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.sellPrice = sellPrice;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDetailForm() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
