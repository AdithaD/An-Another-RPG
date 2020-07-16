package com.ananotherrpg.util;

public class Link<T, S> {
    private T incident;

    private S response;

    public Link(T incident, S response) {
        this.incident = incident;
        this.response = response;
    }


    public T getIncident(){
        return incident;
    }

    public S getResponse(){
        return response;
    }
}
