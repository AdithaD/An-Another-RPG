package com.ananotherrpg.level.quest;

import java.util.List;
/**
 * A inital state version of a <code>Quest</code>, which will be instantiated to a <code>Quest</code> before
 * given to the <code>PlayerAvater</code>
 */
public class QuestTemplate{

    private final int questID;

    private String name;
    private String description;

    List<Objective> objectives;

    /**
     * Instantiates a new quest
     * @return A new quest
     */
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