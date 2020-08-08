package com.ananotherrpg.level;

import com.ananotherrpg.IIdentifiable;
import com.ananotherrpg.util.Link;
/**
 * A connection between two <code>Location</code>s. Acts as an Link (edge) in a <code>LocationGraph</code>.
 */
public class Path extends Link<Location> implements IIdentifiable{

    private int pathID;
    private boolean isTraversible;

    private String name;
    private String description;

    public Path( int pathID, String name, String description, Location node1, Location node2, boolean isTraversible) {
        super(node1, node2);
        this.name = name;
        this.description = description;
        this.isTraversible = isTraversible;
        this.pathID = pathID;
    }

    
    public boolean isTraversible() {
        return isTraversible;
    }

    public void setTraversible(boolean newState){
        this.isTraversible = newState;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getListForm() {
        return "Path: " + name;
    }

    @Override
    public int getID() {
        return pathID;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getDetailForm() {
        return name + ": \n" + description;
    }
}
