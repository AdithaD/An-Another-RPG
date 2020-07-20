package com.ananotherrpg.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.Objective;
import com.ananotherrpg.level.Quest;

public class QuestMananger {
    private List<Quest> completedQuests;
    private List<Quest> activeQuests;
    private Map<Integer, Quest> questLookup;

    public QuestMananger(List<Quest> questLookup){
        this.questLookup = questLookup.stream().collect(Collectors.toMap(Quest::getId, Function.identity()));
        this.activeQuests = new ArrayList<Quest>();
        this.completedQuests = new ArrayList<Quest>();
    }

    public QuestMananger(List<Quest> questLookup, List<Quest> activeQuests, List<Quest> completedQuests){
        this.questLookup = questLookup.stream().collect(Collectors.toMap(Quest::getId, Function.identity()));
        this.activeQuests = activeQuests;
    }

    public void viewQuests() {
		if (activeQuests.isEmpty()) {
			IOManager.println("I have no active quests... ");
		} else {
			IOManager.println("Your current quests are: ");
			IOManager.listStrings(activeQuests.stream().map(Quest::getName).collect(Collectors.toList()),
					ListType.NUMBERED);

			IOManager.println("Enter a number to get more information, or type exit to back out");

			Optional<Quest> opQuest = IOManager.queryUserInputAgainstCustomMap(activeQuests, Quest::getName,
					SelectionMethod.NUMBERED);

			if (opQuest.isPresent()) {
				Quest selectedQuest = opQuest.get();

				IOManager.listStrings(selectedQuest.getObjectives().stream().map(Objective::getDescription)
						.collect(Collectors.toList()), ListType.BULLET);
			} else {
				IOManager.println("You awaken from your deep concentration");
			}
		}
	}

    public Quest getQuestFromId(int questId){
        return questLookup.get(questId);
    }

    public List<Quest> getActiveQuests(){
        return activeQuests;
    }

	public void addNewQuestsById(List<Integer> newQuests) {
        newQuests.stream().forEach(e -> activeQuests.add(questLookup.get(e)));
    }

    private void updateActiveQuests(){
        for (Quest quest : activeQuests) {
            if (quest.isComplete()) {
                activeQuests.remove(quest);
                completedQuests.add(quest);
            }
        }
    }
    
}
