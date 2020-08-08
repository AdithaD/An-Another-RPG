package com.ananotherrpg.entity.dialogue;
/**
 * A <code>DialogueLine</code> that returns an ID for a <code>Path</code> when traversed
 */
public class PathDialogueLine extends DialogueLine{

    private int pathID;

    public PathDialogueLine(int localID, String line, int pathID) {
        super(localID, line);
        this.pathID = pathID;
    }

    @Override
    public void visit(DialogueTraverser dialogueTraverser) {
        dialogueTraverser.recordPath(pathID);
    }
    
}