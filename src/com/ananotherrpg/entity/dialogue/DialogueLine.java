package com.ananotherrpg.entity.dialogue;

public class DialogueLine {
    private String dialogue;

    public DialogueLine(String dialogue){
        this.dialogue = dialogue;
    }

    public String getDialogue(){
        return dialogue;
    }

    public void visit(DialogueManager manager){
        manager.accept(this);
    }
}
