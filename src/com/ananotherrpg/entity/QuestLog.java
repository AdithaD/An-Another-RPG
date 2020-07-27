package com.ananotherrpg.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ananotherrpg.level.Quest;

public class QuestLog {
    private List<Quest> acceptedQuests;
    private List<Quest> completedQuests;

    public QuestLog(List<Quest> acceptedQuests, List<Quest> completedQuests){
        this.acceptedQuests = acceptedQuests;
        this.completedQuests = completedQuests;
    }

    public QuestLog(){
        acceptedQuests = new ArrayList<Quest>();
        completedQuests = new ArrayList<Quest>();
    }

	public void addQuest(Quest quest) {
        if(!containsQuest(quest.getID())) acceptedQuests.add(quest);
    }
    
    private boolean containsQuest(int ID){
        return acceptedQuests.stream().filter(e -> e.getID() == ID).count() > 0;
    }

    public List<Quest> getAcceptedQuests(){
        return Collections.unmodifiableList(acceptedQuests);
    }
}
