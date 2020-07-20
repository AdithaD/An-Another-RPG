package com.ananotherrpg.entity.dialogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.Quest;
import com.ananotherrpg.util.DirectedDataLink;
import com.ananotherrpg.util.DirectedDataGraph;

public class DialogueManager {

    private DirectedDataGraph<DialogueLine, String> dialogueGraph;
    private DialogueLine currentLine;

    private Map<Integer, Quest> camapignQuestLookup;
    private List<Integer> newQuestIds;

    public DialogueManager(List<Quest> campaignQuestLookup) {
        this.camapignQuestLookup = campaignQuestLookup.stream().collect(Collectors.toMap(Quest::getId, Function.identity()));
        this.newQuestIds = new ArrayList<Integer>();
    }

    public void initiateDialogue(Entity entity){
        dialogueGraph = entity.getDialogueGraph();
        currentLine = dialogueGraph.getFirstNode();

		IOManager.println(getDialogue());
		while (hasMoreDialogue()) {
			Map<String, DirectedDataLink<DialogueLine, String>> linkToLinkDataMap = dialogueGraph.getLinks(currentLine).stream()
            .collect(Collectors.toMap(DirectedDataLink::getResponse, Function.identity()));;

			List<String> optionsText = new ArrayList<String>(linkToLinkDataMap.keySet());

			String opLinkData = IOManager.listAndQueryUserInputAgainstStringsWithoutExit(optionsText, ListType.NUMBERED,
					SelectionMethod.NUMBERED);

			traverseLink(linkToLinkDataMap.get(opLinkData));
			IOManager.println(getDialogue());
		}

    }

    public void traverseLink(DirectedDataLink<DialogueLine, String> linkToTraverse){
        currentLine = linkToTraverse.getIncident();
        currentLine.visit(this);
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

	public List<Quest> getNewQuests() {
        List<Quest> newQuests  = newQuestIds.stream().map(e -> camapignQuestLookup.get(e)).collect(Collectors.toList());
        newQuests.removeAll(newQuests);
		return newQuests;
    }
    
    public static final DirectedDataGraph<DialogueLine,String> NO_DIALOGUE;
	
    static{
        DialogueLine noDialogueLine = new DialogueLine("They don't seem interested in talking");

        HashMap<DialogueLine, List<DirectedDataLink<DialogueLine,String>>> noDialogueMap = new HashMap<DialogueLine, List<DirectedDataLink<DialogueLine,String>>>();
        noDialogueMap.putIfAbsent(noDialogueLine, new ArrayList<DirectedDataLink<DialogueLine, String>>());

        NO_DIALOGUE = new DirectedDataGraph<DialogueLine,String>(noDialogueMap, noDialogueLine);
    }
}