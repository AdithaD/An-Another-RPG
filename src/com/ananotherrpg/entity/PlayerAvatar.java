package com.ananotherrpg.entity;

import java.util.List;

import com.ananotherrpg.entity.dialogue.DialogueTraverser;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.level.Location;
import com.ananotherrpg.level.QuestTemplate;

/**
 * The Player class is responsible for all data related to a player in a
 * campaign
 */
public class PlayerAvatar {

	private boolean shouldExitCampaign = false;

	private Entity playerEntity;

	private QuestLog questLog;

	private Location currentLocation;
	private List<Integer> knownPaths;

	public Entity getEntity() {
		return playerEntity;
	}

	public PlayerAvatar(Entity playerEntity, Location currentLocation, List<Integer> knownPaths, QuestLog questLog) {
		this.playerEntity = playerEntity;
		this.currentLocation = currentLocation;
		this.questLog = questLog;
		this.knownPaths = knownPaths;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void talkWith(Entity entity) {
		DialogueTraverser traverser = entity.startDialogue(playerEntity);

		traverser.start();

		for (QuestTemplate template : traverser.getQuestTemplates()) {
			questLog.addQuest(template.instantiateTemplate());
		}
	}

	public void pickUp(ItemStack itemStack) {
		playerEntity.getInventory().addToInventory(itemStack);
		currentLocation.removeItem(itemStack);
	}

	public void learnPath(int pathID){
		knownPaths.add(pathID);
	}

	public void use(Item item){
		playerEntity.use(item);
	}
	
	public QuestLog getQuestLog() {
		return questLog;
	}

	public void moveTo(Location location) {
		currentLocation = location;
	}

	public List<Integer> getKnownPathIDs() {
		return knownPaths;
	}
}
