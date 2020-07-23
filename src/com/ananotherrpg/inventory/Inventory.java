package com.ananotherrpg.inventory;

import java.util.Map;
import java.util.stream.Collectors;

public class Inventory {

	private Map<Item, Integer> items;
	

	public void addToInventory(Item item, int quantity) {
		items.putIfAbsent(item, quantity);
	}

	public void removeFromInventory(Item item, int quantity) {
		items.replace(item, items.get(item).intValue() - quantity);

		if(items.get(item).intValue() <= 0) items.remove(item);
	}


	public int getTotalWeight() {
		int totalWeight = 0;
		for (Item i : items.keySet()) {
			totalWeight += i.getWeight();
		}
		return totalWeight;
	}

	public boolean hasID(int itemID) {
		return !items.keySet().stream().filter(e -> e.getID() == itemID).collect(Collectors.toList()).isEmpty();
	}
}