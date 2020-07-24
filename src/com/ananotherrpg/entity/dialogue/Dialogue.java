package com.ananotherrpg.entity.dialogue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Player;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.util.DirectedDataGraph;

public class Dialogue {
    private DirectedDataGraph<DialogueLine, Response> dialogueGraph;
    private DialogueLine startingLine;

    private DialogueLine currentLine;

    private List<Integer> newfoundLocations;
    private List<Integer> newfoundQuests;

    public Dialogue(DirectedDataGraph<DialogueLine, Response> dialogueGraph, DialogueLine startingLine) {
        this.dialogueGraph = dialogueGraph;
        this.startingLine = startingLine;
    }

	public void start(Player player){
        currentLine = startingLine;
        
        newfoundLocations = new ArrayList<Integer>();
        newfoundQuests = new ArrayList<Integer>();

        loop(player);
    }

    private void loop(Player player){
        IOManager.println(currentLine);
        while (hasNextLine()) {
            updateResponses(player);
            Response response = askPlayerFoResponse();

            nextLine(response);
            IOManager.println(currentLine);
        }
    }

    private void nextLine(Response response){
        currentLine = response.getIncident();
    }

    private Response askPlayerFoResponse(){
        //TODO Implete nextLine in Dialogue
        List<String> responses = dialogueGraph.getLinks(currentLine).stream().map(Response::getResponseText).collect(Collectors.toList());
        return null;
    }

    private boolean hasNextLine(){
        return dialogueGraph.getLinks(currentLine).isEmpty();
    }

    private void updateResponses(Player player){
        dialogueGraph.getLinks(currentLine).forEach(e -> e.reevaluateActiveStatus(player));
    }

    public List<Integer> getNewfoundLocations() {
        return newfoundLocations;
    }


    public List<Integer> getNewfoundQuests() {
        return newfoundQuests;
    }

    public static Dialogue NO_DIALOGUE;
    static{
        final DialogueLine noDialogueLine1 = new DialogueLine("They don't seem to be in a talking mood");
		DirectedDataGraph<DialogueLine, Response> noDialogueGraph = new DirectedDataGraph<DialogueLine, Response>();
        noDialogueGraph.addNode(noDialogueLine1);
        NO_DIALOGUE = new Dialogue(noDialogueGraph, noDialogueLine1);
    }
}
