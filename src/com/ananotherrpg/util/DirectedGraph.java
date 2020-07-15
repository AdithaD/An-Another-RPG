package com.ananotherrpg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectedGraph<T> {

	protected Map<T, List<T>> adjacentNodes;
	
	public DirectedGraph() {
		adjacentNodes = new HashMap<T, List<T>>();
	}
	
	public DirectedGraph(Map<T, List<T>> adjacentNodes) {
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
	
	public void addEdge(T start, T end) {
		adjacentNodes.get(start).add(end);
	}
	
	public void removeEdge(T start, T end) {
		adjacentNodes.get(start).remove(end);
	}

	public List<T> getAdjacentNodes(T node){
		return adjacentNodes.get(node);
	}
}
