package com.ananotherrpg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph<T> {

	private Map<T, List<T>> adjacentNodes;
	
	public Graph() {
		adjacentNodes = new HashMap<T, List<T>>();
	}
	
	public Graph(Map<T, List<T>> adjacentNodes) {
		super();
		this.adjacentNodes = adjacentNodes;
	}

	public void addNode(T newNode) {
		adjacentNodes.putIfAbsent(newNode, new ArrayList<T>());
	}
	
	public void removeNode(T targetNode) {
		adjacentNodes.values().stream().forEach(e -> e.remove(targetNode));
		adjacentNodes.remove(targetNode);
	}
	
	public void addEdge(T node1, T node2) {
		adjacentNodes.get(node1).add(node2);
		adjacentNodes.get(node2).add(node1);
	}
	
	public void removeEdge(T node1, T node2) {
		adjacentNodes.get(node1).remove(node2);
		adjacentNodes.get(node2).remove(node1);
	}

	public List<T> getAdjacentNodes(T node){
		return adjacentNodes.get(node);
	}
}
