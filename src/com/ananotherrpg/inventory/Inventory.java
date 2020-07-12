package com.ananotherrpg.inventory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Inventory {
	
	private LinkedList<ItemStack> items;
	private int totalWeight;
	
	public Inventory() {
		items = new LinkedList<ItemStack>();
		totalWeight = 0;
	}
	public Inventory(LinkedList<ItemStack> items) {
		this.items = items;
	}
	
	public void addToInventory(Item item) {
		
	}
	
	public void addToInventory(ItemStack itemStack) {
		
	}
	
	public void removeFromInventory(Item item) {
		
	}
	
	public void removeFromInventory(ItemStack itemStack) {
		
	}
	
	public void updateTotalWeight() {
		totalWeight = 0;
		for(Iterator<ItemStack> i = items.iterator(); i.hasNext(); ) {
			totalWeight += i.next().getWeight(); 
		}
	}
}
