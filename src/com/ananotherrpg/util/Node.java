package com.ananotherrpg.util;

public class Node<T> {
	private String label;
	private T data;
	
	public Node(String label, T data){
		this.label = label;
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public String getLabel() {
		return label;
	}

}
