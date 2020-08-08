package com.ananotherrpg.entity;

import java.util.List;

import com.ananotherrpg.entity.inventory.Item;
import com.ananotherrpg.entity.inventory.ItemStack;
import com.ananotherrpg.level.Location;
import com.ananotherrpg.level.Path;
import com.ananotherrpg.level.quest.Quest;

/**
 * A representation of the player in the world, its interactions with the world around it and the player's knowledge.
 */
public class PlayerAvatar {

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

	/**
	 * Takes an item from the world and gives it to the player.
	 */
	public void pickUp(ItemStack itemStack) {
		playerEntity.getInventory().addToInventory(itemStack);
		currentLocation.removeItem(itemStack);
	}

	public void traverse(Path path) {
		currentLocation = path.getOther(currentLocation);
	}

	public void learnQuest(Quest newQuest) {
		questLog.addQuest(newQuest);
	}

	public void learnPath(int pathID) {
		knownPaths.add(pathID);
	}

	public void use(Item item) {
		playerEntity.use(item);
	}

	public QuestLog getQuestLog() {
		return questLog;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public List<Integer> getKnownPathIDs() {
		return knownPaths;
	}


}
