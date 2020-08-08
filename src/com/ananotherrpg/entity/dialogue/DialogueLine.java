package com.ananotherrpg.entity.dialogue;

/**
 * A line to be said in a dialogue. It is used as the <code>node</code> in a
 * DialogueGraph.
 */
public class DialogueLine {
    // The ID of the DialogueLine internally to the DialogueGraph. It is used in
    // loading and saving.
    private final int localID;
    private String line;

    public DialogueLine(int localID, String line) {
        this.line = line;
        this.localID = localID;
    }

    /**
     * When this <code>DialogueLine</code> is visited during traversal of the <code>DialogueGraph</code>
     *  it belongs to by a <code>DialogueTraverser</code>, this function is called. 
     * 
     * <p> This allows DialogueLines to pass data back up to the traverser.
     * @param dialogueTraverser The traverser that has come across this <code>DialogueLine</code>
     */
    public void visit(DialogueTraverser dialogueTraverser) {

    }

    public int getLocalID() {
        return localID;
    }

    public String getLine() {
        return line;
    }
}
