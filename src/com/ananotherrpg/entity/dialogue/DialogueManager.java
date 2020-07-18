package com.ananotherrpg.entity.dialogue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.util.Link;
import com.ananotherrpg.util.LinkedDirectedGraph;

public class DialogueManager {

    private IOManager io;
    private LinkedDirectedGraph<DialogueLine, String> dialogueGraph;
    private DialogueLine currentLine;

    private List<Integer> newQuestIds;

    public DialogueManager(LinkedDirectedGraph<DialogueLine, String> dialogueGraph, IOManager io) {
        this.io = io;
        this.dialogueGraph = dialogueGraph;
        this.currentLine = dialogueGraph.getFirstNode();
        this.newQuestIds = new ArrayList<Integer>();
    }

    public void initiateDialogue(){
        
		io.println(getDialogue());
		while (hasMoreDialogue()) {
			Map<String, Link<DialogueLine, String>> linkToLinkDataMap = generateLinkToLinkDataMap();
			List<String> optionsText = new ArrayList<String>(linkToLinkDataMap.keySet());

			String opLinkData = io.listAndQueryUserInputAgainstStringsWithoutExit(optionsText, ListType.NUMBERED,
					SelectionMethod.NUMBERED);

			traverseLink(linkToLinkDataMap.get(opLinkData));
			io.println(getDialogue());
		}

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
        newQuestIds.add(questDialogueLine.getQuest());
    }

    public DialogueLine getCurrentLine(){
        return currentLine;
    }

    public String getDialogue(){
        return currentLine.getDialogue();
    }

	public List<Integer> getNewQuests() {
		return newQuestIds;
	}
}