package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.entity.Player;
import com.ananotherrpg.level.Quest;

public class QuestDialogueLine extends DialogueLine {

	private Quest quest;
	
	public QuestDialogueLine(String dialogue, Quest quest) {
		super(dialogue);
		this.quest = quest;
	}

	public Quest getQuest() {
		return quest;
	}
	
	@Override
	public void visit(DialogueManager manager){
		manager.accept(this);
	}
}
