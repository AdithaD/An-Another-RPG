package com.ananotherrpg.level;

import java.util.List;

import com.ananotherrpg.entity.Entity;

public class KillObjective implements IObjective {

	private String name;

	private List<Entity> targets;

	@Override
	public boolean isComplete() {
		if (targets.stream().allMatch(e  -> e.isDead())) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public String getDescription() {
		//TODO Implement KillObjective description
		return "";
	}

	@Override
	public String getName() {
		return name;
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
