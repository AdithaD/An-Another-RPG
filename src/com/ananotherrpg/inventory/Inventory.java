package com.ananotherrpg.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Inventory {

	private Map<Item, Integer> items;

	/**
	 * Is null if no weapon is equipped;
	 *  
	 * */ 
	private Weapon equippedWeapon;

	public void addToInventory(Item item, int quantity) {
		if(items.containsKey(item)) {
			items.replace(item, quantity + items.get(item).intValue());
		}else{
			items.put(item, quantity);
		};
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

	public void equipWeapon(Weapon weapon){
		if(!hasID(weapon.getID())) addToInventory(weapon, 1);
		equippedWeapon = weapon;
	}

	/**
	 * Gets the weapon equipped
	 * @return The weapon equipped, or NULL if no weapons are equipped
	 */
	public Weapon getEquippedWeapon(){
		return equippedWeapon;
	}



	public Inventory(){
		items = new HashMap<Item, Integer>();

		equippedWeapon = null;
	}

	public Inventory(Map<Item, Integer> items, Weapon equippedWeapon) {
		this.items = items;
		this.equippedWeapon = equippedWeapon;
	}
}