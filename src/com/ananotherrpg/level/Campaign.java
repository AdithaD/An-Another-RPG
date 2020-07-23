package com.ananotherrpg.level;

import java.util.List;
/**
 * Stores all the data related to world excluding the player entity
 */
public class Campaign {

	private String introduction;

	// Maps an Entity ID to an generic Instance of that 
	private List<EntityTemplate> entityTemplates;

	private CampaignState campaignState;

	public CampaignState getCampaignState(){
		return campaignState;
	}

	public String getIntroduction(){
		return introduction;
	}
}
