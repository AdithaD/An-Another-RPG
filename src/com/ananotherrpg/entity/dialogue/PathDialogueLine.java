package com.ananotherrpg.entity.dialogue;

public class PathDialogueLine extends DialogueLine{

    private int pathID;

    public PathDialogueLine(String line, int pathID) {
        super(line);
        this.pathID = pathID;
    }

    @Override
    public void visit(DialogueTraverser dialogueTraverser) {
        dialogueTraverser.recordPath(pathID);
    }
    
}