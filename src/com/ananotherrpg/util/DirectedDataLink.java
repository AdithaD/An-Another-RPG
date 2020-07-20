package com.ananotherrpg.util;

public class DirectedDataLink<T, S>{
    private T incident;
    
    
    private S response;

    public DirectedDataLink(T incident, S response) {
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
