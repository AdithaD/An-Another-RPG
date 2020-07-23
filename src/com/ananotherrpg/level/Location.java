package com.ananotherrpg.level;

import java.util.ArrayList;
import java.util.List;

import com.ananotherrpg.IIdentifiable;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.inventory.Inventory;

public class Location implements IIdentifiable {

	private String name;
	private String description;

	private ArrayList<Entity> entities;

	private Inventory itemsOnGround;

	public Location(String name, String description, ArrayList<Entity> entities,
			Inventory itemsOnGround) {
		super();
		this.name = name;
		this.description = description;
		this.entities = entities;
		this.itemsOnGround = itemsOnGround;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public List<Entity> getPermanentEntities() {
		return entities;
	}

	public Inventory getItemStacks() {
		return itemsOnGround;
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
