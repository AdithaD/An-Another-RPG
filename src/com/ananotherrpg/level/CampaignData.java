package com.ananotherrpg.level;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.inventory.Item;
/**
 * Stores all the static data related to world around the player
 */
public class CampaignData {

	private String name;
	private String introduction;

	// Maps an Entity ID to a template of that 
	private Map<Integer, EntityTemplate> entityTemplates;
	private Map<Integer, QuestTemplate> questTemplates;

	private Map<Integer, Item> items;

	public String getIntroduction(){
		return introduction;
	}

	public Quest instantiateQuestByID(int ID){
		if(!questTemplates.containsKey(ID)) throw new IllegalArgumentException("Quest ID doesn't exist");

		return questTemplates.get(ID).instantiateTemplate();
	}

	public Entity instantiateEntityByID(int ID, int level){
		if(!questTemplates.containsKey(ID)) throw new IllegalArgumentException("Entity ID doesn't exist");

		return entityTemplates.get(ID).instantiateTemplate(ID, level);
	}

	public Item getItemByID(int ID){
		return items.get(ID);
	}

	public CampaignData(String name, String introduction, List<EntityTemplate> entityTemplates,
			List<QuestTemplate> questTemplates, List<Item> campaignItems) {
		this.name = name;
		this.introduction = introduction;

		this.entityTemplates = entityTemplates.stream().collect(Collectors.toMap(EntityTemplate::getID, Function.identity()));
		this.questTemplates = questTemplates.stream().collect(Collectors.toMap(QuestTemplate::getID, Function.identity()));;

		this.items = campaignItems.stream().collect(Collectors.toMap(Item::getID, Function.identity()));

		//this.campaignState = new CampaignState(location);
	}
}
