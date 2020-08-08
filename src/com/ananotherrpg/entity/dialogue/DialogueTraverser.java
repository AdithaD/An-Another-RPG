package com.ananotherrpg.entity.dialogue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.quest.QuestTemplate;

/**
 * This class traverses an <code>Entity</code>'s <code>Dialogue</code> and
 * stores any new player knowledge (Quest, Location/Links)
 */
public class DialogueTraverser {

    private DialogueGraph dialogue;
    private DialogueLine currentLine;

    private Entity source;
    private Entity player;

    // Currently unused
    private List<DialogueLine> traversedLines;

    private List<QuestTemplate> questTemplates;
    private List<Integer> newPathIDs;

    public DialogueTraverser(DialogueGraph dialogue, DialogueLine startingLine, Entity source, Entity traverser) {
        this.dialogue = dialogue;
        currentLine = startingLine;

        this.source = source;
        this.player = traverser;

        this.traversedLines = new ArrayList<DialogueLine>();
        traversedLines.add(currentLine);

        questTemplates = new ArrayList<QuestTemplate>();
        newPathIDs = new ArrayList<Integer>();
    }

    /**
     * Begins the dialogue loop. Will exit once a
     * {@linkplain DialogueGraph#isTerminal(DialogueLine) terminal}
     * <code>DialogueLine</code> is reached.
     */
    public void start() {
        IOManager.println(source.getName() + " says: " + currentLine.getLine());
        while (!dialogue.isTerminal(currentLine)) {
            Response response = askPlayerFoResponse().get();
            nextLine(response);
            IOManager.println(source.getName() + " says: " + currentLine.getLine());
        }
    }

    /**
     * Queries the user for <code>Response</code>s from the <code>currentLine</code>
     */
    private Optional<Response> askPlayerFoResponse() {
        List<Response> viableResponses = dialogue.getViableResponses(source, currentLine, player);
        Optional<Response> viableResponse;
        if (viableResponses.isEmpty()) {
            IOManager.println("You have been left speechless...");
            viableResponse = Optional.empty();
        } else {
            viableResponse = IOManager.listAndQueryUserInputAgainstIQueryables(viableResponses, ListType.NUMBERED,
                    SelectionMethod.NUMBERED, false);

        }
        return viableResponse;
    }

    /**
     * Moves the traverser onto the next <code>DialogueLine</code> based on the
     * response selected.
     * 
     * @param response The reponse the player selected.
     */
    private void nextLine(Response response) {
        currentLine = response.getIncident();
        currentLine.visit(this);
        traversedLines.add(currentLine);
    }

    public List<QuestTemplate> getQuestTemplates() {
        return Collections.unmodifiableList(questTemplates);
    }

    public List<Integer> getNewPathIDs() {
        return newPathIDs;
    }

    /**
     * For use with the visitor pattern,
     * 
     * @param questTemplate The questTemplate to record
     */
    public void recordQuest(QuestTemplate questTemplate) {
        questTemplates.add(questTemplate);
    }

    /**
     * For use with the visitor pattern,
     * 
     * @param pathID The questID to record
     */
    public void recordPath(int pathID) {
        newPathIDs.add(pathID);
    }

}