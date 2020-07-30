package com.ananotherrpg.entity.dialogue;

public class DialogueLine {
    private final int localID;
    private String line;

    public DialogueLine(int localID, String line){
        this.line = line;
        this.localID = localID;
    }

    public int getLocalID() {
        return localID;
    }

    public String getLine(){
        return line;
    }

	public void visit(DialogueTraverser dialogueTraverser) {
        
	}
}
