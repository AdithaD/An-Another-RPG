package com.ananotherrpg.entity.inventory;

import com.ananotherrpg.IIdentifiable;
import com.ananotherrpg.entity.Entity;

public class Item implements IIdentifiable{
	private int itemID;

	protected String name;
	protected String description;

	private String interactText;

	protected int weight;
	protected int sellPrice;

	private boolean consumeOnUse;

	public Item(int itemID, String name, String description, String interactText, int weight, int sellPrice, boolean consumeOnUse) {
		this.itemID = itemID;
		this.name = name;
		this.description = description;

		this.interactText = interactText;

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

	public String getInteractText() {
		return interactText;
	}
	
}
