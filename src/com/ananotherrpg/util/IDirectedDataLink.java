package com.ananotherrpg.util;

public interface IDirectedDataLink<T>{
    public T getIncident();

    public boolean isTraversible();
}
