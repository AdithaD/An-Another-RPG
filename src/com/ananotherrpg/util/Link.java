package com.ananotherrpg.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Link<T>{
    private List<T> nodes;

    private boolean isActive;

    public Link(T node1, T node2, boolean isActive) {
        nodes = new ArrayList<T>();
        nodes.add(node1);
        nodes.add(node2);

        this.isActive = isActive;
    }

    public boolean isEquivalent(Link<T> otherLink) {
       return nodes.equals(otherLink.nodes);
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean newState){
        this.isActive = newState;
    }

    public T getOther(T node){
        if(node == nodes.get(0)){
            return nodes.get(1);
        }else if(node == nodes.get(1)){
            return nodes.get(0);
        }else{
            throw new InvalidParameterException();
        }
    }

    public List<T> getNodes(){
        return nodes;
    }

    public boolean isActive(){
        return isActive;
    }
}
