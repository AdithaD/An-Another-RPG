package com.ananotherrpg.level;

public abstract class Objective{
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
	
	public abstract String getDescription();

	public abstract boolean isComplete();
}
