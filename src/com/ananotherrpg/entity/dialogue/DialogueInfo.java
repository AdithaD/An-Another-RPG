package com.ananotherrpg.entity.dialogue;

/**
 * DialogueInfo
 */
public class DialogueInfo {
    public enum DialogueInfoType{
        QUEST, ITEM, LINK
    }

    private final int ID;
    private final DialogueInfoType type;

    public DialogueInfo(int ID, DialogueInfoType type) {
        this.type = type;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
    
    public DialogueInfoType getType() {
        return type;
    }

}