package com.ananotherrpg.level;

import java.util.List;

public class QuestTemplate{

    private final int questID;

    private String name;
    private String description;

    List<Objective> objectives;

    public Quest instantiateTemplate() {
        return new Quest(questID, name, description, objectives);
    }

    public QuestTemplate(int questID, String name, String description, List<Objective> objectives) {
        this.questID = questID;
        this.name = name;
        this.description = description;
        this.objectives = objectives;
    }
    public int getID(){
        return questID;
    }
}