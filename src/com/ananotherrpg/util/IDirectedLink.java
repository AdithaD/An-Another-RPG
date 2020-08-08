package com.ananotherrpg.util;
/**
 * A edge in a DirectedGraph
 * @param <T> The type of node it is incident to
 */
public interface IDirectedLink<T>{
    public T getIncident();
}
