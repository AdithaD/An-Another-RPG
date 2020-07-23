package com.ananotherrpg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectedDataGraph<T, S extends IDirectedDataLink<T>> {
    private Map<T, List<S>> incidenceMap;
    
    public DirectedDataGraph(Map<T, List<S>> incidenceMap){
        this.incidenceMap = incidenceMap;
    }

    public DirectedDataGraph() {
        this.incidenceMap = new HashMap<T, List<S>>();
	}

	public void addNode(T node){
        incidenceMap.putIfAbsent(node, new ArrayList<S>());

    }

    public void addLink(T origin, S newLink){
        incidenceMap.get(origin).add(newLink);

    }

    public void removeNode(T node){
        incidenceMap.remove(node);

        for (T line : incidenceMap.keySet()) {
            List<S> incidentLinks = incidenceMap.get(line);
            incidentLinks
            .stream()
            .filter(link -> link.getIncident().equals(node))
            .forEach(link -> incidentLinks.remove(link));
        }
    }

    public void removeLink(T node, S link){
        incidenceMap.get(node).remove(link);
    }

	public boolean hasNextDialogue(T line) {
		return !incidenceMap.get(line).isEmpty();
    }

    public List<S> getLinks(T line){
        return incidenceMap.get(line);
    }
    
}