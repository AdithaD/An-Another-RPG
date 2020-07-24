package com.ananotherrpg.level;

import com.ananotherrpg.event.EventData;

public class KillObjective extends Objective {

	private int quantity;
	private int amountKilled;

	public KillObjective(String name, int targetID, int quantity) {
		this.name = name;
		this.targetID = targetID;
		this.quantity = quantity;

		amountKilled = 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getListForm() {
		return "Kill Objective: " + name;
	}

	@Override
	public boolean isComplete() {
		return amountKilled >= quantity;
	}

	@Override
	public void update(EventData data) {
		if(data.getID() == targetID){
			amountKilled += 1;
		}

	}

}
