package com.ananotherrpg.entity.dialogue;

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