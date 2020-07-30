package com.ananotherrpg.entity.dialogue;

import java.util.List;

import com.ananotherrpg.entity.Entity;

/**
 * The layer where entity state can affect dialogue
 */
public class Dialogue {
    //private DirectedGraph<DialogueLine, Response> dialogueGraph;
    private DialogueGraph dialogueGraph;
    //This may change after each interaction with an enitity
    private DialogueLine startingLine;

    public Dialogue(DialogueGraph dialogueGraph, DialogueLine startingLine) {
        this.dialogueGraph = dialogueGraph;
        this.startingLine = startingLine;
    }

    public Dialogue(DialogueGraph dialogueGraph) {
        this.dialogueGraph = dialogueGraph;
        this.startingLine = dialogueGraph.getFirstLine();
    }


    public DialogueTraverser getTraverser(Entity source, Entity target){
        return new DialogueTraverser(this, startingLine, source, target);
    }

    public List<Response> getViableResponses(Entity source, DialogueLine currentLine, Entity traverser) {
		return dialogueGraph.getViableResponses(source, currentLine, traverser);
    }
    
    public boolean isTerminal(DialogueLine line) {
		return dialogueGraph.isTerminal(line);
    }

	public DialogueLine getStartingLine() {
		return startingLine;
  }
  
  public int getGraphID(){
    return dialogueGraph.getID();
  }

}
