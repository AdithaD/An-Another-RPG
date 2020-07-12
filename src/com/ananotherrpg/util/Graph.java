package com.ananotherrpg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph<T> {

	private Map<Node<T>, List<Node<T>>> adjacentNodes;
	
	public Graph() {
		adjacentNodes = new HashMap<Node<T>, List<Node<T>>>();
	}
	
	public Graph(Map<Node<T>, List<Node<T>>> adjacentNodes) {
		super();
		this.adjacentNodes = adjacentNodes;
	}



	public void addNode(Node<T> newNode) {
		adjacentNodes.putIfAbsent(newNode, new ArrayList<Node<T>>());
	}
	
	public void removeNode(Node<T> targetNode) {
		adjacentNodes.values().stream().forEach(e -> e.remove(targetNode));
		adjacentNodes.remove(targetNode);
	}
	
	public void addEdge(Node<T> node1, Node<T> node2) {
		adjacentNodes.get(node1).add(node2);
		adjacentNodes.get(node2).add(node1);
	}
	
	public void removeEdge(Node<T> node1, Node<T> node2) {
		adjacentNodes.get(node1).remove(node2);
		adjacentNodes.get(node2).remove(node1);
	}

	public List<Node<T>> getAdjacentNodes(Node<T> node){
		return adjacentNodes.get(node);
	}
}
