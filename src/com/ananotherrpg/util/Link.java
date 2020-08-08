package com.ananotherrpg.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * An edge in a <code>Graph</code>
 * @param <T> The type of node it links between
 */
public class Link<T>{
    private List<T> nodes;


    public Link(T node1, T node2) {
        nodes = new ArrayList<T>();
        nodes.add(node1);
        nodes.add(node2);

    }

    public boolean isEquivalent(Link<T> otherLink) {
       return nodes.equals(otherLink.nodes);
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

}
