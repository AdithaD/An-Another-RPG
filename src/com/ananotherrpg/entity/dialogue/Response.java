package com.ananotherrpg.entity.dialogue;

import com.ananotherrpg.entity.Player;
import com.ananotherrpg.util.IDirectedDataLink;

public class Response implements IDirectedDataLink<DialogueLine>{

    private DialogueLine incidentLine;
    private String responseText;
    protected boolean isActive;

    @Override
    public DialogueLine getIncident() {
        return incidentLine;
    }

    @Override
    public boolean isTraversible() {
        return isActive;
    }

    public void reevaluateActiveStatus(Player player){
        
    }

    public String getResponseText() {
        return responseText;
    }

}
