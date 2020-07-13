package com.ananotherrpg.level;

import java.util.ArrayList;

import com.ananotherrpg.Identifiable;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.inventory.ItemStack;

public class Location implements Identifiable{
		private int locationId;
		
		private String name;
		
		private ArrayList<Entity> permanentEntities;
		private ArrayList<ItemStack> items;
		
		public Location(int locationId) {
			super();
			this.locationId = locationId;
		}

		public Location(String name, ArrayList<Entity> permanentEntities, ArrayList<ItemStack> items) {
			super();
			this.name = name;
			this.permanentEntities = permanentEntities;
			this.items = items;
		}

		public String getName() {
			return name;
		}

		public ArrayList<Entity> getPermanentEntities() {
			return permanentEntities;
		}	
		
}
