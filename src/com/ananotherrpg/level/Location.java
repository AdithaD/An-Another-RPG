package com.ananotherrpg.level;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.inventory.ItemStack;

public class Location {
		private int locationId;
		
		private String name;
		
		private Entity[] permanentEntities;
		private ItemStack[] items;
		
		public Location(int locationId) {
			super();
			this.locationId = locationId;
		}

		public Location(String name, Entity[] permanentEntities, ItemStack[] items) {
			super();
			this.name = name;
			this.permanentEntities = permanentEntities;
			this.items = items;
		}

		public String getName() {
			return name;
		}	
		
}
