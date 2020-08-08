package com.ananotherrpg.entity.dialogue;

import java.util.List;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.util.DirectedGraph;
/**
 * A wrapper for the <code>directedGraph</code> that holds the dialogue data.
 * Contains methods for querying the <code>directedGraph</code>.
 */
public class DialogueGraph {
    private final int ID;

    private DirectedGraph<DialogueLine, Response> directedGraph;

    // This is the first line that the entity will EVER say.
    private DialogueLine firstLine;

    public int getID() {
        return ID;
    }

    /**
     * Returns the <code>Response</code>s attached to the <code>currentLine</code> that are viable between the source entity and the player entity.
     * Viability is defined by calling <code>Response.isViable(source, player)</code> and is dependent on the type of response.
     * @param source The entity that is speaking to the player
     * @param currentLine The line of which its responses will be filtered by viability.
     * @param player The player entity
     * @return Retuns a <code>List</code> of <code>Response</code>s stemming from the currentLine
     *  which are viable in the current state between the player and source entity.
     */
    public List<Response> getViableResponses(Entity source, DialogueLine currentLine, Entity player) {
		return directedGraph.getLinks(currentLine).stream().filter(e -> e.isViable(source, player)).collect(Collectors.toList());
	}

    /**
     * Returns if there is anymore dialogue after this <code>DialogueLine</code>, by checking if it has any <code>Response</code>s attached.
     * @param line The <code>DialogueLine</code> to check
     * @return If the <code>DialogueLine</code has no responses attached.
     */
	public boolean isTerminal(DialogueLine line) {
		return !directedGraph.hasNextNode(line);
    }

    public DialogueLine getFirstLine() {
        return firstLine;
    }

    public DialogueGraph(int ID, DirectedGraph<DialogueLine, Response> directedGraph, DialogueLine firstLine) {
        this.ID = ID;
        this.firstLine = firstLine;
        this.directedGraph = directedGraph;
    }

}
