package com.ananotherrpg.level;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.dialogue.DialogueGraph;
import com.ananotherrpg.entity.inventory.Item;
import com.ananotherrpg.level.quest.Quest;
import com.ananotherrpg.level.quest.QuestTemplate;
/**
 * Stores all the stateless data related to world around the player
 */
public class CampaignData {

	private String name;
	private String introduction;

	// Maps an Entity ID to a template of that 
	private Map<Integer, EntityTemplate> entityTemplates;
	private Map<Integer, DialogueGraph> dialogueGraphs;

	private Map<Integer, QuestTemplate> questTemplates;

	private Map<Integer, Item> items;

	public CampaignData(String name, String introduction, List<EntityTemplate> entityTemplates, List<DialogueGraph> dialogueGraphs,
			List<QuestTemplate> questTemplates, List<Item> campaignItems) {
		this.name = name;
		this.introduction = introduction;

		this.dialogueGraphs = dialogueGraphs.stream().collect(Collectors.toMap(DialogueGraph::getID, Function.identity()));;
		this.entityTemplates = entityTemplates.stream().collect(Collectors.toMap(EntityTemplate::getID, Function.identity()));
		this.questTemplates = questTemplates.stream().collect(Collectors.toMap(QuestTemplate::getID, Function.identity()));;

		this.items = campaignItems.stream().collect(Collectors.toMap(Item::getID, Function.identity()));

		//this.campaignState = new CampaignState(location);
	}
	
	public String getName() {
		return name;
	}

	public String getIntroduction(){
		return introduction;
	}

	public Quest instantiateQuestByID(int questID){
		if(!questTemplates.containsKey(questID)) throw new IllegalArgumentException("Quest ID doesn't exist");

		return questTemplates.get(questID).instantiateTemplate();
	}

	public Entity instantiateEntityByID(int entityID, int level){
		if(!entityTemplates.containsKey(entityID)) throw new IllegalArgumentException("Entity ID doesn't exist");

		return entityTemplates.get(entityID).instantiateTemplate(level, items, Collections.unmodifiableMap(dialogueGraphs));
	}

	public Item getItemByID(int ID){
		return items.get(ID);
	}

	public QuestTemplate getQuestTemplate(int questID) {
		return questTemplates.get(questID);
	}

	public DialogueGraph getDialogueByID(int ID) {
		return dialogueGraphs.get(ID);
	}
}
