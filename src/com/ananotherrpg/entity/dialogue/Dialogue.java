package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.entity.Entity;

/**
 * Encapsulates all entity specific effects on a <code>dialogueGraph</code>.
 * <p>
 * Contains state such as which line to start when beginning a conversation with
 * this entity.
 */
public class Dialogue {
	private DialogueGraph dialogueGraph;

	// This may change after each interaction with an enitity
	private DialogueLine startingLine;

	public Dialogue(DialogueGraph dialogueGraph, DialogueLine startingLine) {
		this.dialogueGraph = dialogueGraph;
		this.startingLine = startingLine;
	}

	public Dialogue(DialogueGraph dialogueGraph) {
		this.dialogueGraph = dialogueGraph;
		this.startingLine = dialogueGraph.getFirstLine();
	}

	/**
	 * Instantiates a <code>DialogueTraverser</code> between a source entity and the
	 * player entity, starting at the <code>startingLine</code>.
	 * 
	 * @param source The entity that is speaking to the player using this Dialogue
	 * @param player The player entity
	 * @return A <code>DialogueTraverser</code> between a source entity and the
	 *         player entity
	 */
	public DialogueTraverser getTraverser(Entity source, Entity player) {
		return new DialogueTraverser(dialogueGraph, startingLine, source, player);
	}

	public int getGraphID() {
		return dialogueGraph.getID();
	}

	public DialogueLine getStartingLine() {
		return startingLine;
	}

	public void setStartingLine(DialogueLine newStartingLine) {
		startingLine = newStartingLine;
	}

}
