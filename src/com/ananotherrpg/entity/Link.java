package com.ananotherrpg.entity;

public class Link {
    private DialogueLine incident;

    private String response;

    public Link(DialogueLine incident, String response) {
        this.incident = incident;
        this.response = response;
    }


    public DialogueLine getIncident(){
        return incident;
    }

    public String getResponse(){
        return response;
    }
}
