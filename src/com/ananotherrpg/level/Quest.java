package com.ananotherrpg.level;

import java.util.List;

import com.ananotherrpg.IIdentifiable;

public class Quest implements IIdentifiable{

	private String name;
	private String description;

	private List<? extends IObjective> objectives;
	private boolean isActive; 


	public boolean isComplete(){
		boolean isComplete = true;
		for (IObjective obj : objectives) {
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String getListForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDetailForm() {
		// TODO Auto-generated method stub
		return null;
	}
}
