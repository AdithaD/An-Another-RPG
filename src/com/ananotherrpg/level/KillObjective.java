package com.ananotherrpg.level;

import java.util.ArrayList;

import com.ananotherrpg.entity.Combatant;

public class KillObjective extends Objective {

	private ArrayList<Combatant> killTargets;
	
	public KillObjective(int objectiveId, String description, Location location, ArrayList<Combatant> killTargets) {
		super(objectiveId, description, location);
		this.killTargets = killTargets;
	}

	@Override
	public boolean isComplete() {
		if (killTargets.stream().allMatch(e  -> e.IsDead())) {
			return true;
		}else {
			return false;
		}
	}

	public String getDescription() {
		return description;
	}

}
