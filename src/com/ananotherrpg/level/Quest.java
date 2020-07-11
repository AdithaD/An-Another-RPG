package com.ananotherrpg.level;

import java.util.List;

public class Quest {
	private int questId;
	
	private List<Objective> objectives;
	private List<Quest> subquests;
	
	private boolean isComplete;
	private boolean isActive;
}
