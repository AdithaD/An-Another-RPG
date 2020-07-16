package com.ananotherrpg.util;

import java.util.*;

public class LinkedDirectedGraph<T, S>{ 
    private Map<T, List<Link<T, S>>> incidenceMap;
    private T firstNode;
    
    public LinkedDirectedGraph(Map<T, List<Link<T, S>>> incidenceMap, T firstNode){
        this.incidenceMap = incidenceMap;
        this.firstNode = firstNode;
    }

    public LinkedDirectedGraph(T firstNode) {
        this.firstNode = firstNode;
        this.incidenceMap = new HashMap<T, List<Link<T, S>>>();
	}

	public void addNode(T dialogueLine){
        incidenceMap.putIfAbsent(dialogueLine, new ArrayList<Link<T,S>>());

    }

    public void addLink(T origin, Link<T,S> newLink){
        incidenceMap.get(origin).add(newLink);

    }

    public void removeNode(T dialogueLine){
        incidenceMap.remove(dialogueLine);

        for (T line : incidenceMap.keySet()) {
            List<Link<T, S>> incidentLinks = incidenceMap.get(line);
            incidentLinks
            .stream()
            .filter(link -> link.getIncident().equals(dialogueLine))
            .forEach(link -> incidentLinks.remove(link));
        }
    }

    public void removeLink(T dialogueLine, Link<T,S> link){
        incidenceMap.get(dialogueLine).remove(link);
    }

    public T getFirstNode(){
        return firstNode;
    }

	public boolean hasNextDialogue(T line) {
		return !incidenceMap.get(line).isEmpty();
    }

    public List<Link<T,S>> getLinks(T line){
        return incidenceMap.get(line);
    }
}