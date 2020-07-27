package com.ananotherrpg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.PlayerAvatar;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.CampaignData;
import com.ananotherrpg.level.CampaignState;
import com.ananotherrpg.level.Location;
import com.ananotherrpg.level.Quest;

/**
 *  Responsible for user interaction with the campaign
 */
public class Campaign {
    
    private final ArrayList<String> options = new ArrayList<String>(Arrays.asList(new String[] { "Look around",
    "Move to", "Examine", "Talk", "View Quests", "View Inventory", "Pick Up", "Help" }));

	private CampaignData campaignData;
	private CampaignState campaignState;
	
    private PlayerAvatar player;

    private boolean shouldExitCampaign = false;

    public Campaign(CampaignData campaignData, CampaignState campaignState, PlayerAvatar player){
		this.campaignData = campaignData;
		this.campaignState = campaignState;
        this.player = player;
    }

    public void play() {
		while (!shouldExitCampaign) {
			IOManager.println("What would you like to do? (Type help for options)");
			Optional<String> opInput = IOManager.queryUserInputAgainstStrings(options, SelectionMethod.TEXT, true);

			if (opInput.isPresent()) {
				switch (opInput.get()) {
					case "Look around":
						lookAround();
						break;
					case "Move to":
						moveTo();
						break;
					case "Talk":
						talk();
						break;
					case "View Quests":
						viewQuests();
						break;
					case "View Inventory":
						viewInventory();
						break;
					case "Pick Up":
						pickUp();
						break;
					case "Examine":
						examine();
						break;
					case "Help":
						IOManager.listStrings(options, ListType.ONE_LINE);
						break;
				}
			} else {
				IOManager.println("Returning to main menu");
				shouldExitCampaign = true;
			}
		}

    }
    
    public void talk() {
		if (!player.getCurrentLocation().hasEntities()) {
			IOManager.println("There is no one to talk to");
		} else {
			IOManager.println("Who would you like to talk to?");
			Optional<Entity> opEntity = IOManager.listAndQueryUserInputAgainstIQueryables(
					player.getCurrentLocation().getPermanentEntities(), ListType.NUMBERED,
					SelectionMethod.NUMBERED,true);

			if (opEntity.isPresent()) {
				player.talkWith(opEntity.get());
				
			} else {
				IOManager.println("You decide otherwise");
			}
		}
	}

	public void moveTo() {
		List<Location> accessibleLocations = campaignState.getLocationManager().getAccessibleLocationsFrom(player.getCurrentLocation(), player.getKnownPathIDs());
		if (accessibleLocations.isEmpty()) {
			IOManager.println("There is no where to go to!");
		} else {
			IOManager.println("Where would you like to go ?");

			Optional<Location> opLocation = IOManager.listAndQueryUserInputAgainstIQueryables(accessibleLocations, ListType.NUMBERED, SelectionMethod.NUMBERED, true);

			if (opLocation.isPresent()) {
				player.moveTo(opLocation.get());
				IOManager.println("You move to " + player.getCurrentLocation().getName());
			} else {
				IOManager.println("Staying here is fine for now...");
			}

		}

	}

	public void lookAround() {
		IOManager.println("You take a quick whirl around and you see: ");
		IOManager.listIQueryables(player.getCurrentLocation().getIIdentifiables(), ListType.BULLET);

	}

	public void examine() {
		List<IIdentifiable> identifiables = player.getCurrentLocation().getIIdentifiables();

		IOManager.println("What would you like to examine: (Type its name)");
		Optional<? extends IIdentifiable> opIdentifiable = IOManager.listAndQueryUserInputAgainstIQueryables(identifiables, ListType.NUMBERED,
				SelectionMethod.NUMBERED, true);

		if (opIdentifiable.isPresent()) {
			IOManager.println(opIdentifiable.get().getDetailForm());
		} else {
			IOManager.println("You know enough already. Evidently...");
		}
	}

	public void pickUp() {
		Optional<ItemStack> opItem = IOManager.listAndQueryUserInputAgainstIQueryables(
				player.getCurrentLocation().getItemStacks().getItems(), ListType.NUMBERED, SelectionMethod.NUMBERED, true);
		if (opItem.isPresent()) {
            player.pickUp(opItem.get());
			
		} else {
			IOManager.println("Your rucksack is doesn't need another item, you say.");
		}
	}

	public void viewInventory() {
		IOManager.println("Choose an item to inquire about or type exit to go back");
		Optional<ItemStack> opItemStack = IOManager.listAndQueryUserInputAgainstIQueryables(player.getEntity().getInventory().getItems(),ListType.NUMBERED, SelectionMethod.NUMBERED, true);

		ItemStack itemStack;
		if(opItemStack.isPresent()){
			itemStack = opItemStack.get();

			IOManager.println(itemStack.getItem().getDetailForm());
			boolean shouldUse = IOManager.askYesOrNoQuestion("Would you like to use this item?");

			if(shouldUse){
                player.use(itemStack.getItem());
			}
		}
	}

	private void viewQuests() {
		IOManager.listIQueryables(player.getQuestLog().getAcceptedQuests(), ListType.NUMBERED);

		IOManager.println("Select a number to get more details, or type exit to back out!");

		Optional<Quest> opQuest = IOManager.queryUserInputAgainstIQueryable(player.getQuestLog().getAcceptedQuests(), SelectionMethod.NUMBERED, true);

		if(opQuest.isPresent()){
			IOManager.println(opQuest.get().getDetailForm());
		}

	}
}