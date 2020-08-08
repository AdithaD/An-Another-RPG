package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.IQueryable;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.util.IDirectedLink;

/**
 * A speech response to a NPC's line.
 * 
 * <p>An implementation of <code>IDirectedLink</code> to use with a <code>DialogueGraph</code> to link <code>DialogueLine</code>. 
 * 
 *
 */
public class Response implements IDirectedLink<DialogueLine>, IQueryable{

    private final DialogueLine incidentLine;
    private final String responseText;

    public Response(DialogueLine incidentLine, String responseText){
        this.incidentLine = incidentLine;
        this.responseText = responseText;

    }

    @Override
    public DialogueLine getIncident() {
        return incidentLine;
    }

    public String getResponseText() {
        return responseText;
    }

    @Override
    public String getName() {
        return responseText;
    }

    @Override
    public String getListForm() {
        return "Say \"" + responseText + "\"";
    }

    /**
     * A response can be viable based on entity data from the source entity, and the player's entity.
     * 
     * @param source The entity talking to the player
     * @param player The player entity
     * @return If the response is viable, based on the given entity data.
     */
    public boolean isViable(Entity source, Entity player){
        return true;
    }

}
