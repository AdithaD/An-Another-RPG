package com.ananotherrpg.entity;

import java.util.*;

public class DialogueGraph{
    Map<DialogueLine, List<Link>> incidenceMap;
    DialogueLine firstDialogueLine;
    
    public DialogueGraph(Map<DialogueLine, List<Link>> incidenceMap, DialogueLine firstDialogueLine){
        this.incidenceMap = incidenceMap;
        this.firstDialogueLine = firstDialogueLine;
    }

    public DialogueGraph(DialogueLine firstDialogueLine) {
        this.firstDialogueLine = firstDialogueLine;
        this.incidenceMap = new HashMap<DialogueLine, List<Link>>();
	}

	public void addDialogueLine(DialogueLine dialogueLine){
        incidenceMap.putIfAbsent(dialogueLine, new ArrayList<Link>());

    }

    public void addLink(DialogueLine origin, Link newLink){
        incidenceMap.get(origin).add(newLink);

    }

    public void removeDialogueLine(DialogueLine dialogueLine){
        incidenceMap.remove(dialogueLine);

        for (DialogueLine line : incidenceMap.keySet()) {
            List<Link> incidentLinks = incidenceMap.get(line);
            incidentLinks
            .stream()
            .filter(link -> link.getIncident().equals(dialogueLine))
            .forEach(link -> incidentLinks.remove(link));
        }
    }

    public void removeLink(DialogueLine dialogueLine, Link link){
        incidenceMap.get(dialogueLine).remove(link);
    }

    public DialogueLine getFirstDialogueLine(){
        return firstDialogueLine;
    }

	public boolean hasNextDialogue(DialogueLine line) {
		return !incidenceMap.get(line).isEmpty();
    }

    public List<Link> getLinks(DialogueLine line){
        return incidenceMap.get(line);
    }
    
    public static final DialogueGraph NO_DIALOGUE;
    static{
        DialogueLine noDialogueLine = new DialogueLine("They don't seem interested in talking");

        HashMap<DialogueLine, List<Link>> noDialogueMap = new HashMap<DialogueLine, List<Link>>();
        noDialogueMap.putIfAbsent(noDialogueLine, new ArrayList<Link>());

        NO_DIALOGUE = new DialogueGraph(noDialogueMap, noDialogueLine);
    }
}