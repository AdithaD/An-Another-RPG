package com.ananotherrpg.entity.dialogue;

import java.util.List;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.util.DirectedGraph;
/**
 * Houses and queries the pure data structure that contains the dialogue
 */
public class DialogueGraph {
    private final int ID;

    private DirectedGraph<DialogueLine, Response> directedGraph;

    // This is the first line that the entity will EVER say.
    private DialogueLine firstLine;

    public int getID() {
        return ID;
    }

    public List<Response> getViableResponses(Entity source, DialogueLine currentLine, Entity traverser) {
		return directedGraph.getLinks(currentLine).stream().filter(e -> e.isViable(source, traverser)).collect(Collectors.toList());
	}

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
