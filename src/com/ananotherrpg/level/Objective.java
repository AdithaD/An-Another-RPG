package com.ananotherrpg.level;

public abstract class Objective{
	protected int objectiveId;
	protected String description;
	
	protected Location location;
	
	protected Boolean isActive = false;
	protected Boolean isComplete = false;
	
	public Objective(int objectiveId, String description, Location location) {
		super();
		this.objectiveId = objectiveId;
		this.description = description;
		this.location = location;
	}
	
	public int getId(){
		return objectiveId;
	}

	public String getDescription(){
		return description;
	}

	public boolean isComplete(){
		return isComplete;
	}
}
