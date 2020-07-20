package com.ananotherrpg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectedDataGraph<T, S> {
    private Map<T, List<DirectedDataLink<T, S>>> incidenceMap;
    private T firstNode;
    
    public DirectedDataGraph(Map<T, List<DirectedDataLink<T,S>>> incidenceMap, T firstNode){
        this.incidenceMap = incidenceMap;
        this.firstNode = firstNode;
    }

    public DirectedDataGraph(T firstNode) {
        this.firstNode = firstNode;
        this.incidenceMap = new HashMap<T, List<DirectedDataLink<T,S>>>();
	}

	public void addNode(T node){
        incidenceMap.putIfAbsent(node, new ArrayList<DirectedDataLink<T,S>>());

    }

    public void addLink(T origin, DirectedDataLink<T,S> newLink){
        incidenceMap.get(origin).add(newLink);

    }

    public void removeNode(T node){
        incidenceMap.remove(node);

        for (T line : incidenceMap.keySet()) {
            List<DirectedDataLink<T,S>> incidentLinks = incidenceMap.get(line);
            incidentLinks
            .stream()
            .filter(link -> link.getIncident().equals(node))
            .forEach(link -> incidentLinks.remove(link));
        }
    }

    public void removeLink(T node, DirectedDataLink<T,S> link){
        incidenceMap.get(node).remove(link);
    }

    public T getFirstNode(){
        return firstNode;
    }

	public boolean hasNextDialogue(T line) {
		return !incidenceMap.get(line).isEmpty();
    }

    public List<DirectedDataLink<T,S>> getLinks(T line){
        return incidenceMap.get(line);
    }
    
}