package com.ananotherrpg.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Quest{
	private int questId;

	private String questName;
	private Map<Integer, Objective> objectiveIdMap;

	public Quest(int questId, String questName, List<Objective> objectives, boolean isComplete) {
		super();
		this.questId = questId;
		this.objectiveIdMap = objectives.stream().collect(Collectors.toMap(Objective::getId, Function.identity()));
		this.questName = questName;
	}

	public Quest(int questId, List<Objective> objectives, boolean isComplete, boolean isActive) {
		super();
		this.questId = questId;
		this.objectiveIdMap = objectives.stream().collect(Collectors.toMap(Objective::getId, Function.identity()));

	}

	public int getId() {
		return questId;
	}

	public String getName() {
		return questName;
	}

	public List<Objective> getObjectives() {
		return new ArrayList<Objective>(objectiveIdMap.values());
	}

	public boolean isComplete(){
		boolean isComplete = true;
		for (Objective obj : objectiveIdMap.values()) {
			if(!obj.isComplete()){
				isComplete = false;
				break;
			}
		}

		return isComplete;

	}
}
