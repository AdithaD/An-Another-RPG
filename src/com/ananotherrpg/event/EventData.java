package com.ananotherrpg.event;

/**
 * A data package to be pass over by <code>EventDispatcher</code>
 * 
 */
public class EventData {

    private int ID;

    public int getID(){
        return ID;
    }

    public EventData(int iD) {
        ID = iD;
    }

}
