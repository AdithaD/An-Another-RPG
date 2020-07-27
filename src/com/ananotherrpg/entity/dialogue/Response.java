package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.IQueryable;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.util.IDirectedLink;

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

    public boolean isViable(Entity source, Entity target){
        return true;
    }

}
