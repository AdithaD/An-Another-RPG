package com.ananotherrpg.level.quest;

import java.util.List;

import com.ananotherrpg.IIdentifiable;
import com.ananotherrpg.level.CampaignState;

/**
 * A quest for the player to complete. A quest contains many objectives.
 */

public class Quest implements IIdentifiable{

	private final int questID;

	private String name;
	private String description;

	private List<? extends Objective> objectives;

	public Quest(int questID, String name, String description, List<Objective> objectives) {
		this.questID = questID;
		this.name = name;
		this.description = description;
		this.objectives = objectives;
	}

	public boolean isComplete(CampaignState state){
		boolean isComplete = true;
		for (Objective obj : objectives) {
			if(!obj.isComplete()){
				isComplete = false;
				break;
			}
		}
		return isComplete;
	}


	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getListForm() {
		return name;
	}

	@Override
	public String getDetailForm() {
		String detailForm = "\n" 
		+ "Quest: " + name + "\n" 
		+ description + " \n"
		+ "Objectives: " + "\n";

		for (Objective objective : objectives) {
			detailForm += objective.getListForm() + "\n";
		}

		return detailForm;
	}

	@Override
	public int getID() {
		return questID;
	}

	public List<? extends Objective> getObjectives() {
		return objectives;
	}
}
