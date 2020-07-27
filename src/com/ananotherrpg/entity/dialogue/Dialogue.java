package com.ananotherrpg.entity.dialogue;

import java.util.List;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.util.DirectedGraph;

/**
 * Wraps up all data pertaining to conversing with an entity
 */
public class Dialogue {
    private DirectedGraph<DialogueLine, Response> dialogueGraph;

    //This may change after each interaction with an enitity
    private DialogueLine startingLine;

    public Dialogue(DirectedGraph<DialogueLine, Response> dialogueGraph, DialogueLine startingLine) {
        this.dialogueGraph = dialogueGraph;
        this.startingLine = startingLine;
    }


    public DialogueTraverser getTraverser(Entity source, Entity target){
        return new DialogueTraverser(this, startingLine, source, target);
    }

	public List<Response> getViableResponses(Entity source, DialogueLine currentLine, Entity traverser) {
		return dialogueGraph.getLinks(currentLine).stream().filter(e -> e.isViable(source, traverser)).collect(Collectors.toList());
	}

	public boolean isTerminal(DialogueLine line) {
		return !dialogueGraph.hasNextNode(line);
    }
    
    
    public static Dialogue NO_DIALOGUE;
    static{
        final DialogueLine noDialogueLine1 = new DialogueLine("They don't seem to be in a talking mood");
		DirectedGraph<DialogueLine, Response> noDialogueGraph = new DirectedGraph<DialogueLine, Response>();
        noDialogueGraph.addNode(noDialogueLine1);
        NO_DIALOGUE = new Dialogue(noDialogueGraph, noDialogueLine1);
    }
}
