package com.ananotherrpg.level;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    
    public List<Path> getAccessiblePathsFrom(Location location, List<Integer> knownIntegersIDs) {
		
		return maximalGraph.getKnownAccessiblePaths(location, knownIntegersIDs);
	}

	public Location getLocation(int ID) {
		return maximalGraph.findLocation(ID);
    }
    
    public Set<Location> getAllLocations(){
        return Collections.unmodifiableSet(maximalGraph.getAllLocations());
    }

	public Set<Path> getAllPaths() {
		return Collections.unmodifiableSet(maximalGraph.getAllPaths());
	}

}
