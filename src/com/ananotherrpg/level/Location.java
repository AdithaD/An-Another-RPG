package com.ananotherrpg.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.ananotherrpg.IIdentifiable;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.inventory.Inventory;
import com.ananotherrpg.entity.inventory.ItemStack;
/**
 * A place in the world where entities, including the player, can be.
 */
public class Location implements IIdentifiable {

	private final int locationID;

	private String name;
	private String description;

	private List<Entity> entities;

	private Inventory itemsOnGround;

	public Location(int locationID, String name, String description, List<Entity> entities,
			Inventory itemsOnGround) {
		super();
		this.locationID = locationID;
		this.name = name;
		this.description = description;
		this.entities = entities;
		this.itemsOnGround = itemsOnGround;
	}

	public boolean hasEntities() {
		return !entities.isEmpty();
	}

	public void removeItem(ItemStack itemStack) {
		itemsOnGround.removeFromInventory(itemStack);
	}
	
	@Override
	public String getName() {
		return name;
	}

	public List<Entity> getPermanentEntities() {
		return Collections.unmodifiableList(entities);
	}

	public Inventory getInventory() {
		return itemsOnGround;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getListForm() {
		return "Location: " + name;
	}

	@Override
	public String getDetailForm() {
		return "Location: " + name + " \\n " + description;
	}

	@Override
	public int getID() {
		return locationID;
	}

	public List<IIdentifiable> getIIdentifiables() {
		List<IIdentifiable> identifiables = new ArrayList<IIdentifiable>();

		identifiables.addAll(entities);
		identifiables.addAll(itemsOnGround.getItems().stream().map(ItemStack::getItem).collect(Collectors.toList()));
		return identifiables;
	}

}
