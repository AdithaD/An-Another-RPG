package com.ananotherrpg.level;

import com.ananotherrpg.*;
public abstract class Objective implements Identifiable{
	protected int objectiveId;
	protected String description;
	
	protected Location location;
	
	protected Boolean isActive = false;
	
	public Objective(int objectiveId, String description, Location location) {
		super();
		this.objectiveId = objectiveId;
		this.description = description;
		this.location = location;
	}
	
	public abstract boolean isComplete();
}
