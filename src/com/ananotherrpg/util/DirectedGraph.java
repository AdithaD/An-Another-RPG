package com.ananotherrpg.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * An implementation of a directed graph data structure.
 * @param <T> The "node" type
 * @param <S> The "edge" type, must implement IDirectedLink
 */
public class DirectedGraph<T, S extends IDirectedLink<T>> {
    private Map<T, List<S>> incidenceMap;
    
    public DirectedGraph(Map<T, List<S>> incidenceMap){
        this.incidenceMap = incidenceMap;
    }

    public DirectedGraph() {
        this.incidenceMap = new HashMap<T, List<S>>();
	}

	public void addNode(T node){
        incidenceMap.putIfAbsent(node, new ArrayList<S>());

    }

    public void addLink(T origin, S newLink){
        incidenceMap.get(origin).add(newLink);

    }

    public void removeNode(T target){
        incidenceMap.remove(target);

        for (T node : incidenceMap.keySet()) {
            List<S> incidentLinks = incidenceMap.get(node);
            incidentLinks
            .stream()
            .filter(link -> link.getIncident().equals(node))
            .forEach(link -> incidentLinks.remove(link));
        }
    }

    public void removeLink(T node, S link){
        incidenceMap.get(node).remove(link);
    }

	public boolean hasNextNode(T node) {
		return !incidenceMap.get(node).isEmpty();
    }
	public boolean contains(T node) {
		return incidenceMap.containsKey(node);
	}

	public List<S> getLinks(T node) {
		return Collections.unmodifiableList(incidenceMap.get(node));
	}
    
}