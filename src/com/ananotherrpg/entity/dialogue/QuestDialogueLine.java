package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.entity.Player;

public class QuestDialogueLine extends DialogueLine {

	private int questID;
	
	public QuestDialogueLine(String dialogue, int questID) {
		super(dialogue);
		this.questID = questID;
	}

	public int getQuestID() {
		return questID;
	}
	
}
