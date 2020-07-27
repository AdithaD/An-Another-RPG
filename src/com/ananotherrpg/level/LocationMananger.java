package com.ananotherrpg.level;

import java.util.List;

public class LocationMananger {
    private LocationGraph maximalGraph;

    public LocationMananger(LocationGraph maximalGraph) {
        this.maximalGraph = maximalGraph;
    }

    public void setPathTraversibility(int pathID, boolean status){
        maximalGraph.setPathTraversability(pathID, status);
    }
    
	public List<Location> getAccessibleLocationsFrom(Location location, List<Integer> knownIntegersIDs) {
		return maximalGraph.getKnownAccessibleLocations(location, knownIntegersIDs);
	}

}
