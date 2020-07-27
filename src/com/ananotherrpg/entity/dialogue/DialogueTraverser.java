package com.ananotherrpg.entity.dialogue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.QuestTemplate;
/**
 * This class traverses a Dialogue instance and stores any new knowledge (Quest, Location/Links, Items)
 */
public class DialogueTraverser {
    
    private Dialogue dialogue;
    private DialogueLine currentLine;

    private Entity source;
    private Entity traverser;

    private List<DialogueLine> traversedLines;

    private List<QuestTemplate> questTemplates;
    private List<Integer> newPathIDs;

    public DialogueTraverser(Dialogue dialogue, DialogueLine startingLine, Entity source, Entity traverser){
        this.dialogue = dialogue;
        currentLine = startingLine;

        this.source = source;
        this.traverser = traverser;

        this.traversedLines = new ArrayList<DialogueLine>();
        traversedLines.add(currentLine);

        questTemplates = new ArrayList<QuestTemplate>();
        newPathIDs = new ArrayList<Integer>();
    }

    public void start(){
        IOManager.println(source.getName() + " says: " + currentLine.getLine());
        while (!dialogue.isTerminal(currentLine)) {
            Optional<Response> response = askPlayerFoResponse();

            if(!response.isPresent()){
                IOManager.println("You've been left utterly speechless");
                break;
            }else{
                nextLine(response.get());
                IOManager.println(source.getName() + " says: " + currentLine.getLine());
            }
        }
    }

    private Optional<Response> askPlayerFoResponse() {
        List<Response> viableResponses = dialogue.getViableResponses(source, currentLine, traverser);
        Optional<Response> viableResponse;
        if(viableResponses.isEmpty()){
            IOManager.println("You have been left speechless..."); 
            viableResponse =  Optional.empty();
        }else{
            viableResponse = IOManager.listAndQueryUserInputAgainstIQueryables(viableResponses, ListType.NUMBERED, SelectionMethod.NUMBERED, false);
            
        }
        return viableResponse;
    }

    private void nextLine(Response response) {
        currentLine = response.getIncident();
        currentLine.visit(this);
        traversedLines.add(currentLine);
    }

    public List<QuestTemplate> getQuestTemplates(){
        return Collections.unmodifiableList(questTemplates);
    }

    /**
     * For use with the visitor pattern,
     * @param questID The ID of the quest to record
     */
	public void recordQuest(QuestTemplate questTemplate) {
        questTemplates.add(questTemplate);
    }
    
    public void recordPath(int pathID){
        newPathIDs.add(pathID);
    }

	public List<Integer> getNewPathIDs() {
		return newPathIDs;
	}
}