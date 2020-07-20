package com.ananotherrpg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Graph<T>{ 
    private Map<T, List<Link<T>>> adjacencyMap;

    public Graph(){
        adjacencyMap = new HashMap<T, List<Link<T>>>();
    }
    
    public Graph(Map<T, List<Link<T>>> incidenceMap){
        this.adjacencyMap = incidenceMap;
    }

	public void addNode(T node){
        adjacencyMap.putIfAbsent(node, new ArrayList<Link<T>>());

    }

    public void addLink(Link<T> newLink){
        List<T> nodes = newLink.getNodes();

        for (T t : nodes) {
            adjacencyMap.get(t).add(newLink);
        }
    }

    public void addLink(T node1, T node2, boolean isActive){
        Link<T> node12Link = new Link<T>(node1, node2, isActive);
        adjacencyMap.get(node1).add(node12Link);
        adjacencyMap.get(node2).add(node12Link);
    }

    public void removeNode(T node){
        //Maps the nodes adjacent to the given node to the links in the given nodes adjaceny map
        Map<T, Link<T>> connectedNodesToLinkMap = adjacencyMap.get(node).stream()
        .collect(Collectors.toMap(e -> e.getOther(node), Function.identity()));

        // Iterates through the connect nodes and removes the link that binds them to the given node
        for (T t : connectedNodesToLinkMap.keySet()) {
            adjacencyMap.get(t).remove(connectedNodesToLinkMap.get(t));
        }
    }

    public void removeLink(Link<T> link){
        List<T> nodes = link.getNodes();

        for (T t : nodes) {
            adjacencyMap.get(t).remove(link);
        }
    }

    public List<Link<T>> getLinks(T node){
        return adjacencyMap.get(node);
    }

	public List<T> getAdjacentNodes(T node) {
		return adjacencyMap.get(node).stream().map(e -> e.getOther(node)).collect(Collectors.toList());
    }
    
    public List<T> getAccessibleNodes(T node){
        return adjacencyMap.get(node).stream()
        .filter(e -> e.isActive()) // gets only active nodes
        .map(e-> e.getOther(node)) // gets the opposite node on the link
        .collect(Collectors.toList()); 
    }
}