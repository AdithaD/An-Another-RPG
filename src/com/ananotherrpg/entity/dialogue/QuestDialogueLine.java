package com.ananotherrpg.entity.dialogue;

public class QuestDialogueLine extends DialogueLine {

	private int questID;
	
	public QuestDialogueLine(String dialogue, int questID) {
		super(dialogue);
		this.questID = questID;
	}

	public int getQuest() {
		return questID;
	}
	
	@Override
	public void visit(DialogueManager manager){
		manager.accept(this);
	}
}
