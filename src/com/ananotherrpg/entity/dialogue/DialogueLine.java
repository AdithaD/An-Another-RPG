package com.ananotherrpg.entity.dialogue;

public class DialogueLine {
    private String line;

    public DialogueLine(String line){
        this.line = line;
    }

    public String getLine(){
        return line;
    }

	public void visit(DialogueTraverser dialogueTraverser) {
        
	}
}
