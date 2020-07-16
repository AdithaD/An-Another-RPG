package com.ananotherrpg.entity.dialogue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.level.Quest;
import com.ananotherrpg.util.Link;
import com.ananotherrpg.util.LinkedDirectedGraph;

public class DialogueManager {
    private LinkedDirectedGraph<DialogueLine, String> dialogueGraph;
    private DialogueLine currentLine;

    private List<Quest> newQuests;

    public DialogueManager(LinkedDirectedGraph<DialogueLine, String> dialogueGraph) {
        this.dialogueGraph = dialogueGraph;
        this.currentLine = dialogueGraph.getFirstNode();
        this.newQuests = new ArrayList<Quest>();
    }

    public void traverseLink(Link<DialogueLine, String> linkToTraverse){
        currentLine = linkToTraverse.getIncident();
        currentLine.visit(this);
    }

    public Map<String, Link<DialogueLine, String>> generateLinkToLinkDataMap(){
        return dialogueGraph.getLinks(currentLine).stream()
					.collect(Collectors.toMap(Link::getResponse, Function.identity()));
    }


    public boolean hasMoreDialogue(){
        return dialogueGraph.hasNextDialogue(currentLine);
    }

    public void accept(DialogueLine dialogueLine) {
        
	}

    public void accept(QuestDialogueLine questDialogueLine){
        newQuests.add(questDialogueLine.getQuest());
    }

    public DialogueLine getCurrentLine(){
        return currentLine;
    }

    public String getDialogue(){
        return currentLine.getDialogue();
    }

	public List<Quest> getNewQuests() {
		return newQuests;
	}
}