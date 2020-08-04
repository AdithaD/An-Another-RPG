package com.ananotherrpg.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Entity;

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

	public void addToInventory(ItemStack itemStack) {
		if(items.containsKey(itemStack.getItem())) {
			items.replace(itemStack.getItem(), itemStack.getQuantity() + items.get(itemStack.getItem()).intValue());
		}else{
			items.put(itemStack.getItem(), itemStack.getQuantity());
		};
	}

	public void removeFromInventory(Item item, int quantity) {
		items.replace(item, items.get(item).intValue() - quantity);

		if(items.get(item).intValue() <= 0) items.remove(item);
	}
	public void removeFromInventory(ItemStack itemStacks) {
		items.replace(itemStacks.getItem(), items.get(itemStacks.getItem()).intValue() - itemStacks.getQuantity());

		if(!items.containsKey(itemStacks.getItem())) throw new IllegalArgumentException("Item not in inventory!");
		if(items.get(itemStacks.getItem()).intValue() <= 0) items.remove(itemStacks.getItem());
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

	public String getDetails(){
		String details = "Items: \n";

		for (Entry<Item, Integer> entry : items.entrySet()) {
			details += entry.getKey().getListForm() + " x" + entry.getValue().intValue();
		}

		return details;
	}

	public List<ItemStack> getItems() {
		List<ItemStack> itemStacks = new ArrayList<ItemStack>();
	
		for (Entry<Item, Integer> entry : items.entrySet()) {
			itemStacks.add(new ItemStack(entry.getKey(), entry.getValue()));
		}

		return itemStacks;
	}

	public Inventory(){
		items = new HashMap<Item, Integer>();

		equippedWeapon = null;
	}

	public Inventory(Map<Item, Integer> items, Weapon equippedWeapon) {
		this.items = items;
		this.equippedWeapon = equippedWeapon;
	}

	public void use(Item item, Entity entity) {

		if(items.containsKey(item)) {
			item.use(entity);
			if(item.shouldConsumeOnUse()) removeFromInventory(item, 1);
		}else{
			throw new IllegalArgumentException("Item to be used not in inventory");
		}
	}

	public boolean hasEquippedWeapon() {
		return equippedWeapon != null;
	}
}