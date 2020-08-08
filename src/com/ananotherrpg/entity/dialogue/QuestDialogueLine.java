package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.level.quest.QuestTemplate;

/**
 * A <code>DialogueLine</code> that returns a <code>QuestTemplate</code> when traversed.
 */
public class QuestDialogueLine extends DialogueLine {

	private QuestTemplate questTemplate;
	
	public QuestDialogueLine(int localID, String dialogue, QuestTemplate questTemplate) {
		super(localID, dialogue);
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
