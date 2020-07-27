package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.level.QuestTemplate;

public class QuestDialogueLine extends DialogueLine {

	private QuestTemplate questTemplate;
	
	public QuestDialogueLine(String dialogue, QuestTemplate questTemplate) {
		super(dialogue);
		this.questTemplate = questTemplate;
	}

	public QuestTemplate getQuestTemplate() {
		return questTemplate;
	}

	@Override
	public void visit(DialogueTraverser dialogueTraverser) {
		dialogueTraverser.recordQuest(questTemplate);
	}
	
}
