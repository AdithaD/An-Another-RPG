package com.ananotherrpg.level;

import java.util.ArrayList;
import java.util.List;

import com.ananotherrpg.Identifiable;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;

public class Location implements Identifiable {
	private int locationId;

	private String name;
	private String description;

	private ArrayList<Entity> permanentEntities;
	private ArrayList<ItemStack> itemStacks;

	public Location(int locationId) {
		super();
		this.locationId = locationId;
	}

	public Location(String name, String description, ArrayList<Entity> permanentEntities,
			ArrayList<ItemStack> itemStacks) {
		super();
		this.name = name;
		this.description = description;
		this.permanentEntities = permanentEntities;
		this.itemStacks = itemStacks;
	}

	public String getName() {
		return name;
	}

	public List<Entity> getPermanentEntities() {
		return permanentEntities;
	}

	public ArrayList<ItemStack> getItemStacks() {
		return itemStacks;
	}

	public void printLocationDetails() {
		IOManager.println(description);
		IOManager.println("You do a quick whirl and you see:");

		if (permanentEntities.isEmpty()) {
			IOManager.println("There is noone in this area");
		} else {
			IOManager.listIdentifiers(permanentEntities, ListType.BULLET);
		}

		if (itemStacks.isEmpty()) {
			IOManager.println("There is no items in this area");
		} else {
			IOManager.listIdentifiers(itemStacks, ListType.BULLET);
		}
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String getDetails() {
		return description;
	}

}
