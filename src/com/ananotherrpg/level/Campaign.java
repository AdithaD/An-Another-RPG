package com.ananotherrpg.level;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.util.Graph;
/**
 * Stores all the data related to world excluding the player entity
 */
public class Campaign {

	private String name;
	private String introduction;

	// Maps an Entity ID to a template of that 
	private Map<Integer, EntityTemplate> entityTemplates;
	private Map<Integer, QuestTemplate> questTemplates;
	
	private CampaignState campaignState;

	public String getIntroduction(){
		return introduction;
	}

	public void addQuest(int questID){
		
	}

	public Campaign(String name, String introduction, List<EntityTemplate> entityTemplates,
			List<QuestTemplate> questTemplates, Graph<Location> locations) {
		this.name = name;
		this.introduction = introduction;

		this.entityTemplates = entityTemplates.stream().collect(Collectors.toMap(EntityTemplate::getID, Function.identity()));
		this.questTemplates = questTemplates.stream().collect(Collectors.toMap(QuestTemplate::getID, Function.identity()));;

		this.campaignState = new CampaignState(locations);
	}
}
