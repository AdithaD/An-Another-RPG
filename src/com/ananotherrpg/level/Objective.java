package com.ananotherrpg.level;

public class Objective {
	private int objectiveId;
	private String description;
	
	private Location location;
	
	private Boolean isComplete = false;
	private Boolean isActive = false;
	
	public Objective(int objectiveId, String description, Location location) {
		super();
		this.objectiveId = objectiveId;
		this.description = description;
		this.location = location;
	}
}
