package com.ananotherrpg.inventory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Inventory {
	
	private LinkedList<ItemStack> itemStacks;
	private Weapon equippedWeapon;
	private int totalWeight;
	
	public Inventory() {
		itemStacks = new LinkedList<ItemStack>();
		totalWeight = 0;
	}
	public Inventory(LinkedList<ItemStack> items) {
		this.itemStacks = items;
	}

	public void addToInventory(Item item) {
		itemStacks.add(new ItemStack(item, 1));
	}
	
	public void addToInventory(ItemStack itemStack) {
		itemStacks.add(itemStack);
	}
	
	public void removeFromInventory(Item item, int quantity) {
		for (int i = 0; i < itemStacks.size(); i++) {
			ItemStack itemStack = itemStacks.get(i);
			if(itemStack.getItem() == item){
				itemStack.reduceQuantityBy(quantity);
				if(itemStack.isEmpty()){
					removeFromInventory(itemStacks.get(i));
				}
			}
		}
	}

	public void removeFromInventory(ItemStack itemStack){
		itemStacks.remove(itemStack);
	}

	public void updateTotalWeight() {
		totalWeight = 0;
		for(Iterator<ItemStack> i = itemStacks.iterator(); i.hasNext(); ) {
			totalWeight += i.next().getWeight(); 
		}
	}
	public List<ItemStack> getItemStacks() {
		return itemStacks;
	}
}
