package com.ananotherrpg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Graph<T, S extends Link<T>>{ 
    private Map<T, List<S>> adjacencyMap;

    public Graph(){
        adjacencyMap = new HashMap<T, List<S>>();
    }
    
    public Graph(Map<T, List<S>> incidenceMap){
        this.adjacencyMap = incidenceMap;
    }

	public void addNode(T node){
        adjacencyMap.putIfAbsent(node, new ArrayList<S>());

    }

    public void addLink(S newLink){
        List<T> nodes = newLink.getNodes();

        for (T t : nodes) {
            adjacencyMap.get(t).add(newLink);
        }
    }

    public void removeNode(T node){
        //Maps the nodes adjacent to the given node to the links in the given nodes adjaceny map
        Map<T, S> connectedNodesToLinkMap = adjacencyMap.get(node).stream()
        .collect(Collectors.toMap(e -> e.getOther(node), Function.identity()));

        // Iterates through the connect nodes and removes the link that binds them to the given node
        for (T t : connectedNodesToLinkMap.keySet()) {
            adjacencyMap.get(t).remove(connectedNodesToLinkMap.get(t));
        }
    }

    public void removeLink(S link){
        List<T> nodes = link.getNodes();

        for (T t : nodes) {
            adjacencyMap.get(t).remove(link);
        }
    }

    public List<S> getLinks(T node){
        return adjacencyMap.get(node);
    }

	public List<T> getAdjacentNodes(T node) {
		return adjacencyMap.get(node).stream().map(e -> e.getOther(node)).collect(Collectors.toList());
    }

	public Set<T> getNodes() {
		return new HashSet<T>(adjacencyMap.keySet());
	}
    
    // public List<T> getAccessibleNodes(T node){
    //     return adjacencyMap.get(node).stream()
    //     .filter(e -> e.isActive()) // gets only active nodes
    //     .map(e-> e.getOther(node)) // gets the opposite node on the link
    //     .collect(Collectors.toList()); 
    // }
}