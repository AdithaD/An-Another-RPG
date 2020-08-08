package com.ananotherrpg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.PlayerAvatar;
import com.ananotherrpg.entity.combat.Combat;
import com.ananotherrpg.entity.dialogue.DialogueTraverser;
import com.ananotherrpg.entity.inventory.ItemStack;
import com.ananotherrpg.event.EventData;
import com.ananotherrpg.event.EventDispatcher;
import com.ananotherrpg.event.EventDispatcher.GameEvent;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.CampaignData;
import com.ananotherrpg.level.CampaignState;
import com.ananotherrpg.level.Path;
import com.ananotherrpg.level.quest.Quest;
import com.ananotherrpg.level.quest.QuestTemplate;

/**
 * Responsible for user interaction with the campaign
 */
public class Campaign {

	private final ArrayList<String> options = new ArrayList<String>(Arrays.asList(new String[] { "Look around",
			"Move to", "Examine", "Talk", "View Quests", "View Inventory", "Pick Up", "Help", "Save", "Fight" }));

	private CampaignData campaignData;
	private CampaignState campaignState;

	private PlayerAvatar player;

	private EventDispatcher eventDispatcher;

	private boolean shouldExitCampaign = false;

	public Campaign(CampaignData campaignData, CampaignState campaignState, PlayerAvatar player) {
		this.campaignData = campaignData;
		this.campaignState = campaignState;
		this.player = player;

		eventDispatcher = new EventDispatcher();
		for (Quest quest : player.getQuestLog().getAcceptedQuests()) {
			quest.getObjectives().stream().forEach(e -> eventDispatcher.register(e));
		}
	}

	/**
	 * Starts the main game loop.
	 */
	public void play() {
		IOManager.println(campaignData.getIntroduction());

		// THE MAIN GAME LOOP
		while (!shouldExitCampaign) {
			IOManager.println("What would you like to do? (Type help for options)");
			Optional<String> opInput = IOManager.queryUserInputAgainstStrings(options, SelectionMethod.TEXT, true);

			if (opInput.isPresent()) {
				String input = opInput.get();
				if (input.equalsIgnoreCase(options.get(0))) { // Look Around
					lookAround();
				} else if (input.equalsIgnoreCase(options.get(1))) { // Move
					moveTo();
				}else if (input.equalsIgnoreCase(options.get(2))) { // Examine
					examine();
				}else if (input.equalsIgnoreCase(options.get(3))) { // Talk
					talk();
				}else if (input.equalsIgnoreCase(options.get(4))) { // View Quests
					viewQuests();
				}else if (input.equalsIgnoreCase(options.get(5))) { // View Inventory
					viewInventory();
				}else if (input.equalsIgnoreCase(options.get(6))) { // Pick Up
					pickUp();
				}else if (input.equalsIgnoreCase(options.get(7))) { // Help
					IOManager.listStrings(options, ListType.ONE_LINE);
				}else if (input.equalsIgnoreCase(options.get(8))) { // Save
					save();
				}else if (input.equalsIgnoreCase(options.get(9))) { // Fight
					fight();
				}
			} else {
				IOManager.println("Returning to main menu");
				shouldExitCampaign = true;
			}
		}

	}

	private void fight() {
		//PLayer selects a target to fight
		IOManager.println("Who do you want to fight?");

		

		Optional<Entity> opEntity = IOManager.listAndQueryUserInputAgainstIQueryables(
				player.getCurrentLocation().getAlivePermanentEntities(), ListType.NUMBERED, SelectionMethod.NUMBERED, true);

		if (opEntity.isPresent()) {
			Entity target = opEntity.get();
			boolean confirmed = IOManager
					.askYesOrNoQuestion("Are you sure you want to fight " + target.getName() + " ?");

			if (confirmed) {
				IOManager.println("Enganging combat with " + target.getName());
				Combat combat = new Combat(player.getEntity(), target);
				combat.start();

				if (player.getEntity().isDead())
					gameOver();
				else {
					combat.getKilledOpponents().stream().forEach(
							e -> eventDispatcher.publish(new EventData(e.getEntity().getID()), GameEvent.KILL));
					IOManager.println("That was tough!");
				}

			} else {
				IOManager.println("Pacifism entices you more...");
			}
		} else {

		}
	}

	private void gameOver() {
		IOManager.println("Game Over!");
		shouldExitCampaign = true;
	}

	private void save() {
		Game.saveGame(campaignState, player);
	}

	private void talk() {
		if (!player.getCurrentLocation().hasAliveEntities()) {
			IOManager.println("There is no one to talk to");
		} else {
			IOManager.println("Who would you like to talk to?");
			Optional<Entity> opEntity = IOManager.listAndQueryUserInputAgainstIQueryables(
					player.getCurrentLocation().getAlivePermanentEntities(), ListType.NUMBERED, SelectionMethod.NUMBERED,
					true);

			if (opEntity.isPresent()) {
				DialogueTraverser traverser = opEntity.get().startDialogue(player.getEntity());

				// Starts the dialogue loop
				traverser.start();

				for (QuestTemplate template : traverser.getQuestTemplates()) {
					Quest newQuest = template.instantiateTemplate();
					player.learnQuest(newQuest);
					newQuest.getObjectives().stream().forEach(e -> eventDispatcher.register(e));
				}

				for (Integer pathID : traverser.getNewPathIDs()) {
					player.learnPath(pathID);
				}

			} else {
				IOManager.println("You decide otherwise");
			}
		}
	}

	private void moveTo() {
		List<Path> accessiblePaths = campaignState.getLocationGraph()
				.getKnownTraversiblePaths(player.getCurrentLocation(), player.getKnownPathIDs());
		if (accessiblePaths.isEmpty()) {
			IOManager.println("There is no where to go to!");
		} else {
			IOManager.println("What path would you like to traverse to?");

			Optional<Path> opPath = IOManager.listAndQueryUserInputAgainstIQueryables(accessiblePaths,
					ListType.NUMBERED, SelectionMethod.NUMBERED, true);

			if (opPath.isPresent()) {
				player.traverse(opPath.get());
				IOManager.println("You move to " + player.getCurrentLocation().getName());
			} else {
				IOManager.println("Staying here is fine for now...");
			}

		}

	}

	private void lookAround() {
		IOManager.println("You take a quick whirl around and you see: ");
		IOManager.listIQueryables(player.getCurrentLocation().getIIdentifiables(), ListType.BULLET);

	}

	private void examine() {
		List<IIdentifiable> identifiables = player.getCurrentLocation().getIIdentifiables();

		IOManager.println("What would you like to examine: (Type its name)");
		Optional<? extends IIdentifiable> opIdentifiable = IOManager.listAndQueryUserInputAgainstIQueryables(
				identifiables, ListType.NUMBERED, SelectionMethod.NUMBERED, true);

		if (opIdentifiable.isPresent()) {
			IOManager.println(opIdentifiable.get().getDetailForm());
		} else {
			IOManager.println("You know enough already. Evidently...");
		}
	}

	private void pickUp() {
		Optional<ItemStack> opItem = IOManager.listAndQueryUserInputAgainstIQueryables(
				player.getCurrentLocation().getInventory().getItems(), ListType.NUMBERED, SelectionMethod.NUMBERED,
				true);
		if (opItem.isPresent()) {
			player.pickUp(opItem.get());

		} else {
			IOManager.println("Your rucksack is doesn't need another item, you say.");
		}
	}

	private void viewInventory() {
		IOManager.println("Choose an item to inquire about or type exit to go back");
		Optional<ItemStack> opItemStack = IOManager.listAndQueryUserInputAgainstIQueryables(
				player.getEntity().getInventory().getItems(), ListType.NUMBERED, SelectionMethod.NUMBERED, true);

		ItemStack itemStack;
		if (opItemStack.isPresent()) {
			itemStack = opItemStack.get();

			IOManager.println(itemStack.getItem().getDetailForm());
			boolean shouldUse = IOManager.askYesOrNoQuestion("Would you like to use this item?");

			if (shouldUse) {
				IOManager.println(itemStack.getItem().getInteractText());
				player.use(itemStack.getItem());
			}
		}
	}

	private void viewQuests() {
		IOManager.println("Accepted Quests: ");
		IOManager.listIQueryables(player.getQuestLog().getAcceptedQuests(), ListType.NUMBERED);

		IOManager.println("Completed Quests: ");
		IOManager.listIQueryables(player.getQuestLog().getCompletedQuests(), ListType.BULLET);

		IOManager.println("Select a number to get more details, or type exit to back out!");

		Optional<Quest> opQuest = IOManager.queryUserInputAgainstIQueryable(player.getQuestLog().getAcceptedQuests(),
				SelectionMethod.NUMBERED, true);

		if (opQuest.isPresent()) {
			IOManager.println(opQuest.get().getDetailForm());
		}

	}
}