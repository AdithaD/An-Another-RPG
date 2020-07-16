package com.ananotherrpg.level;

public class Quest {
	private int questId;
	
	private Objective[] objectives;
	private Quest[] subquests;
	
	private boolean isComplete;
	private boolean isActive;
	public Quest(int questId, Objective[] objectives, Quest[] quests, boolean isComplete, boolean isActive) {
		super();
		this.questId = questId;
		this.objectives = objectives;
		this.subquests = quests;
		this.isComplete = isComplete;
		this.isActive = isActive;
	}
	
	
}
